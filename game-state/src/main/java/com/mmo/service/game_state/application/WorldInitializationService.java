package com.mmo.service.game_state.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mmo.service.game_state.application.dto.*;
import com.mmo.service.game_state.infrastructure.messager.EntityStateProducer;
import com.mmo.service.game_state.infrastructure.messager.WorldStateProducer;
import com.mmo.service.game_state.infrastructure.persistence.entity.WorldItemEntity;
import com.mmo.service.game_state.infrastructure.persistence.repository.PlayerRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class WorldInitializationService {

    private final GameWorldService worldService;
    //private final MobSpawnerService mobSpawnerService;
    private final WorldStateProducer worldStateProducer;
    private final PlayerRepository playerRepository;
    private final EntityStateProducer entityStateProducer;

    public WorldInitializationService(GameWorldService worldService, WorldStateProducer worldStateProducer, PlayerRepository playerRepository, EntityStateProducer entityStateProducer) {
        this.worldService = worldService;
        this.worldStateProducer = worldStateProducer;
        this.playerRepository = playerRepository;
        this.entityStateProducer = entityStateProducer;
    }

    @PostConstruct
    public void initializeWorld() {
        System.out.println("🌍 Inicializando mundo del juego...");

        // Cargar todas las zonas activas
        List<String> activeZones = List.of("castle_courtyard", "forest", "town");

        Flux.fromIterable(activeZones)
                .flatMap(zoneId -> worldService.loadZone(zoneId))
                .doOnNext(zoneData -> System.out.println(""))
                .doOnComplete(() -> System.out.println("🎮 Mundo inicializado correctamente"))
                .subscribe();

        // Iniciar spawner de mobs
        //mobSpawnerService.startSpawning();
    }

    /**
     * Limpieza periódica de items expirados
     */
    @Scheduled(fixedRate = 60000) // Cada minuto
    public void cleanupExpiredItems() {
        /*worldService.removeExpiredItems()
                .doOnSuccess(count -> {
                    if (count > 0) {
                        System.out.println("🧹 Limpiados {} items expirados " + count);
                    }
                })
                .subscribe();*/
    }

    public void initializePlayerAndWorld(WorldInitDTO dto) {
        playerRepository.updateOnlineStatus(dto.getEntityId(), 'Y');
        playerRepository.findById(dto.getEntityId())
                .flatMap(playerEntity -> {
                    // 1️⃣ Enviar primero el jugador
                    try {
                        entityStateProducer.sendPlayerUpdate(new PlayerDTO(playerEntity));
                    } catch (JsonProcessingException e) {
                        return Mono.error(e);
                    }

                    // 2️⃣ Enviar Items
                    Mono<Void> itemsMono = worldService.getItemsNearPosition(
                                    playerEntity.getCurrentZoneId(),
                                    playerEntity.getPosX(),
                                    playerEntity.getPosZ(),
                                    40D
                            )
                            .filter(WorldItemEntity::getIsAvailable)
                            .concatMap(item -> {
                                try {
                                    worldStateProducer.sendItemUpdate(dto.getEntityId(), new WorldItemDTO(item));
                                    return Mono.empty();
                                } catch (JsonProcessingException e) {
                                    return Mono.error(e);
                                }
                            })
                            .then(); // Mono<Void> que completa cuando todos los items se enviaron

                    // 3️⃣ Enviar NPCs
                    Mono<Void> npcsMono = worldService.getNPCsNearPosition(
                                    playerEntity.getCurrentZoneId(),
                                    playerEntity.getPosX(),
                                    playerEntity.getPosZ(),
                                    40D
                            )
                            .concatMap(npc -> {
                                try {
                                    worldStateProducer.sendNPCUpdate(dto.getEntityId(), new NPCDTO(npc));
                                    return Mono.empty();
                                } catch (JsonProcessingException e) {
                                    return Mono.error(e);
                                }
                            })
                            .then();

                    // 4️⃣ Enviar Mobs
                    Mono<Void> mobsMono = worldService.getMobsNearPosition(
                                    playerEntity.getCurrentZoneId(),
                                    playerEntity.getPosX(),
                                    playerEntity.getPosZ(),
                                    40D
                            )
                            .concatMap(mob -> {
                                try {
                                    worldStateProducer.sendMobUpdate(dto.getEntityId(), new MobDTO(mob));
                                    return Mono.empty();
                                } catch (JsonProcessingException e) {
                                    return Mono.error(e);
                                }
                            })
                            .then();

                    // Concatenar todo: Items → NPCs → Mobs
                    return itemsMono.concatWith(npcsMono).concatWith(mobsMono).then(Mono.just(playerEntity));
                })
                .subscribe(
                        player -> System.out.println("✅ Player y mundo inicializados correctamente: " + player.getPlayerId()),
                        err -> System.err.println("❌ Error inicializando jugador y mundo: " + err)
                );
    }

}