package com.mmo.service.game_state.application;


import com.mmo.service.game_state.infrastructure.persistence.entity.PlayerEntity;
import com.mmo.service.game_state.infrastructure.persistence.repository.PlayerRepository;
import com.mmo.service.game_state.application.dto.EntityStateDTO;
import com.mmo.service.game_state.domain.model.ValidatedPosition;
import com.mmo.service.game_state.infrastructure.persistence.entity.ZoneEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service

public class MovementValidationService {

    private final GameWorldService worldService;
    private final PlayerRepository playerRepository;

    @Value("${game.movement.max-speed:10.0}")
    private Double maxSpeed;

    @Value("${game.movement.tolerance:1.1}")
    private Double tolerance;

    @Value("${game.movement.tick-rate:0.1}") // 100ms
    private Double tickRate;

    public MovementValidationService(GameWorldService worldService, PlayerRepository playerRepository) {
        this.worldService = worldService;
        this.playerRepository = playerRepository;
    }

    public Mono<ValidatedPosition> validateMovement(EntityStateDTO dto) {
        String entityId = dto.getEntityId();
        ValidatedPosition.Position requestedPos = ValidatedPosition.Position.fromDTO(dto.getPosition());
        String zoneId = dto.getAttributes().get("zone").toString();

        return playerRepository.findById(entityId)
                .switchIfEmpty(Mono.error(new IllegalStateException("Player no encontrado")))
                .flatMap(player -> {
                    ValidatedPosition.Position currentPos = new ValidatedPosition.Position(
                            player.getPosX(), player.getPosY(), player.getPosZ()
                    );

                    // Calcular metadata
                    double distance = currentPos.distanceTo(requestedPos);
                    double maxDistance = maxSpeed * tickRate * tolerance;

                    ValidatedPosition.ValidationMetadata metadata = ValidatedPosition.ValidationMetadata.create()
                            .distanceMoved(distance)
                            .maxAllowedDistance(maxDistance);

                    // 1. Validar velocidad (anti-cheat)
                    if (distance > maxDistance) {
                        System.out.println("⚠️ Movimiento sospechoso de " +
                                entityId + ": " + distance + " > " + maxDistance);

                        return Mono.just(
                                ValidatedPosition.rejected(currentPos,
                                                "Velocidad excedida: " + String.format("%.2f > %.2f", distance, maxDistance))
                                        .withMetadata(metadata)
                        );
                    }

                    // 2. Verificar límites de la zona
                    return worldService.getZone(zoneId)
                            .flatMap(zone -> {
                                if (!requestedPos.isWithinBounds(
                                        zone.getMinX(), zone.getMaxX(),
                                        zone.getMinZ(), zone.getMaxZ())) {

                                    System.out.println("🚫 Player {} fuera de límites de zona: " + entityId);

                                    // Corregir posición al borde más cercano
                                    ValidatedPosition.Position corrected = clampToZoneBounds(
                                            requestedPos, zone
                                    );

                                    metadata.hadCollision(true).collisionType("boundary");

                                    return updatePlayerPosition(player, corrected)
                                            .thenReturn(
                                                    ValidatedPosition.corrected(corrected, "Límite de zona")
                                                            .withMetadata(metadata)
                                            );
                                }

                                // 3. Verificar colisiones con terreno
                                return worldService.hasTerrainCollision(zoneId,
                                                requestedPos.getX(), requestedPos.getY(), requestedPos.getZ())
                                        .flatMap(hasCollision -> {
                                            if (hasCollision) {
                                                System.out.println("🚫 Colisión con terreno en " +  requestedPos);

                                                // Intentar resolver la colisión
                                                return resolveTerrainCollision(currentPos, requestedPos, zoneId)
                                                        .flatMap(resolvedPos -> {
                                                            metadata.hadCollision(true).collisionType("terrain");

                                                            return updatePlayerPosition(player, resolvedPos)
                                                                    .thenReturn(
                                                                            ValidatedPosition.corrected(resolvedPos,
                                                                                            "Colisión con terreno")
                                                                                    .withMetadata(metadata)
                                                                    );
                                                        });
                                            }

                                            // 4. Todo OK - actualizar posición
                                            return updatePlayerPosition(player, requestedPos)
                                                    .thenReturn(
                                                            ValidatedPosition.accepted(requestedPos)
                                                                    .withMetadata(metadata)
                                                    );
                                        });
                            });
                })
                .onErrorResume(error -> {
                    System.out.println("❌ Error validando movimiento: " + error.getMessage());
                    return Mono.just(ValidatedPosition.error("Error interno: " + error.getMessage()));
                });
    }

    private Mono<Void> updatePlayerPosition(PlayerEntity player, ValidatedPosition.Position position) {
        player.setPosX(position.getX());
        player.setPosY(position.getY());
        player.setPosZ(position.getZ());
        return playerRepository.save(player).then();
    }

    private ValidatedPosition.Position clampToZoneBounds(ValidatedPosition.Position pos, ZoneEntity zone) {
        return new ValidatedPosition.Position(
                Math.max(zone.getMinX(), Math.min(zone.getMaxX(), pos.getX())),
                pos.getY(),
                Math.max(zone.getMinZ(), Math.min(zone.getMaxZ(), pos.getZ()))
        );
    }

    private Mono<ValidatedPosition.Position> resolveTerrainCollision(
            ValidatedPosition.Position from,
            ValidatedPosition.Position to,
            String zoneId) {

        // Estrategia simple: proyectar sobre el plano XZ (deslizar sobre la pared)
        ValidatedPosition.Position resolved = new ValidatedPosition.Position(
                to.getX(),
                from.getY(), // Mantener altura original
                from.getZ()  // No avanzar en Z
        );

        // Verificar si la nueva posición es válida
        return worldService.hasTerrainCollision(zoneId,
                        resolved.getX(), resolved.getY(), resolved.getZ())
                .flatMap(stillCollides -> {
                    if (stillCollides) {
                        // Si aún colisiona, intentar solo en X
                        resolved.setX(from.getX());
                        resolved.setZ(to.getZ());

                        return worldService.hasTerrainCollision(zoneId,
                                        resolved.getX(), resolved.getY(), resolved.getZ())
                                .map(finalCollides -> finalCollides ? from : resolved);
                    }
                    return Mono.just(resolved);
                });
    }
}