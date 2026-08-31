package com.mmo.service.game_state.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mmo.service.game_state.application.dto.EntityStateDTO;
import com.mmo.service.game_state.domain.model.MoveInputData;
import com.mmo.service.game_state.domain.model.MoveResult;
import com.mmo.service.game_state.domain.model.ValidatedPosition;
import com.mmo.service.game_state.infrastructure.messager.EntityStateProducer;
import com.mmo.service.game_state.infrastructure.persistence.repository.PlayerRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class EntityHandlerService {
    private final EntityStateProducer producer;
    private final PlayerRepository playerRepository;
    private final MovementValidationService movementValidationService;
    private static final float GRAVITY = -9.81f;
    private static final float GROUND_OFFSET = -2f;
    private static final float SPEED_CHANGE_RATE = 10f;
    private static final float ROTATION_SMOOTH_TIME = 0.12f;

    private float rotationVelocity = 0f;
    private float animationBlend = 0f;
    private float targetRotation = 0f;


    public EntityHandlerService(EntityStateProducer producer, PlayerRepository playerRepository, MovementValidationService movementValidationService) {
        this.producer = producer;
        this.playerRepository = playerRepository;
        this.movementValidationService = movementValidationService;
    }


    public void movement(EntityStateDTO dto) {
        movementValidationService.validateMovement(dto)
                .doOnNext(validatedPos -> {
                    // Si fue rechazado o corregido, notificar al cliente
                    if (validatedPos.requiresClientUpdate()) {
                        //notifyPositionCorrection(dto.getEntityId(), validatedPos);
                        try {
                            producer.sendEntityUpdate(dto);
                        } catch (Exception e) {
                            System.out.println("Error enviando a Kafka: " + e);
                        }

                    }

                    // Si fue aceptado, continuar con el procesamiento
                    if (validatedPos.isAccepted()) {
                        // Actualizar DTO con posición validada
                        dto.getPosition().setX(validatedPos.getPosition().getX());
                        dto.getPosition().setY(validatedPos.getPosition().getY());
                        dto.getPosition().setZ(validatedPos.getPosition().getZ());

                        // Enviar a Kafka
                        try {
                            producer.sendEntityUpdate(dto);
                        } catch (Exception e) {
                            System.out.println("Error enviando a Kafka: " + e);
                        }

                        // Consultar entorno
                        //checkNearbyEntities(dto).subscribe();
                    }
                })
                .onErrorResume(e -> {
                    System.out.println("Error procesando mensajes: " + e.getMessage());
                    return Mono.empty();
                })
                .then();
    }

    public Mono<Void> handleMovement(MoveInputData input) {
        return playerRepository.findById(input.getEntityId())
                .flatMap(playerEntity -> {
                    // Actualizar input con posición actual
                    input.setPosX(playerEntity.getPosX().floatValue());
                    input.setPosY(playerEntity.getPosY().floatValue());
                    input.setPosZ(playerEntity.getPosZ().floatValue());

                    // Calcular nueva posición
                    MoveResult result;
                    try {
                        result = calculateMovement(input);
                    } catch (JsonProcessingException e) {
                        return Mono.error(e);
                    }

                    // Actualizar en DB y devolver Mono
                    return playerRepository.updatePositionAndZone(
                                    input.getEntityId(),
                                    (double) result.getNewX(),
                                    (double) result.getNewY(),
                                    (double) result.getNewZ()
                            ).doOnNext(update -> System.out.println("Posición actualizada"))
                            .doOnError(err -> System.err.println("Error al actualizar posición: " + err.getMessage()))
                            .then(); // Convertir Mono<PlayerEntity> -> Mono<Void>
                }).then();
    }

    /*
        movementValidationService.validateMovement(dto)
                .doOnNext(validatedPos -> {
                    // Si fue rechazado o corregido, notificar al cliente
                    if (validatedPos.requiresClientUpdate()) {
                        //notifyPositionCorrection(dto.getEntityId(), validatedPos);
                        input.setMoveX(validatedPos.getPosition().getX().floatValue());
                        input.setMoveX(validatedPos.getPosition().getY().floatValue());
                        try {
                            calculateMovement(input);
                        } catch (Exception e) {
                            System.out.println("Error enviando a Kafka: " + e);
                        }

                    }

                    if (validatedPos.isAccepted()) {
                        // Enviar a Kafka
                        try {
                            calculateMovement(input);
                        } catch (Exception e) {
                            System.out.println("Error enviando a Kafka: " + e);
                        }
                    }
                })
                .onErrorResume(e -> {
                    System.out.println("Error procesando mensajes: " + e.getMessage());
                    return Mono.empty();
                })
                .then();
  */
    private MoveResult calculateMovement(MoveInputData input) throws JsonProcessingException {
        // Velocidad objetivo
        float targetSpeed = input.isSprint() ? input.getSprintSpeed() : input.getMoveSpeed();
        animationBlend = input.getAnimationBlend();


        if (input.getMoveX() == 0 && input.getMoveY() == 0) {
            targetSpeed = 0f;
        }

        float speedOffset = 0.1f;
        float inputMagnitude = input.isAnalogMovement()
                ? (float) Math.sqrt(input.getMoveX() * input.getMoveX() + input.getMoveY() * input.getMoveY())
                : 1f;

        float speed = input.getCurrentHorizontalSpeed();

        // Suavizar aceleración/desaceleración
        if (speed < targetSpeed - speedOffset || speed > targetSpeed + speedOffset) {
            speed = lerp(speed, targetSpeed * inputMagnitude, input.getDeltaTime() * SPEED_CHANGE_RATE);
            speed = Math.round(speed * 1000f) / 1000f;
        } else {
            speed = targetSpeed;
        }

        // Animación
        animationBlend = lerp(animationBlend, targetSpeed, input.getDeltaTime() * SPEED_CHANGE_RATE);
        if (animationBlend < 0.01f) animationBlend = 0f;

        // Dirección del input
        float inputX = input.getMoveX();
        float inputZ = input.getMoveY();
        float inputLength = (float) Math.sqrt(inputX * inputX + inputZ * inputZ);
        float dirX = inputLength == 0 ? 0 : inputX / inputLength;
        float dirZ = inputLength == 0 ? 0 : inputZ / inputLength;

        float rotation = input.getRotationY();

        if (inputLength != 0) {
            float[] rotationVelocity = new float[]{input.getRotationVelocity()};
            float targetRot = (float) Math.toDegrees(Math.atan2(dirX, dirZ)) + input.getCameraYaw();
            rotation = smoothDampAngle(rotation, targetRot, rotationVelocity, ROTATION_SMOOTH_TIME, input.getDeltaTime());
            input.setRotationVelocity(rotationVelocity[0]);
            targetRotation = targetRot;
        }

        // Gravedad
        float verticalVelocity = input.getVerticalVelocity();
        if (input.isGrounded()) {
            if (verticalVelocity < 0f) verticalVelocity = -2f; // mantener pegado al suelo
        } else {
            verticalVelocity += GRAVITY * input.getDeltaTime();
        }

        // Dirección final del movimiento
        float rad = (float) Math.toRadians(targetRotation);
        float forwardX = (float) Math.sin(rad);
        float forwardZ = (float) Math.cos(rad);

        float newX = input.getPosX() + forwardX * speed * input.getDeltaTime();
        float newZ = input.getPosZ() + forwardZ * speed * input.getDeltaTime();
        float newY = input.getPosY() + verticalVelocity * input.getDeltaTime();

        MoveResult moveResult = new MoveResult(
                input.getEntityId(),
                "player_move",
                newX, newY, newZ,
                rotation,
                speed,
                animationBlend,
                inputMagnitude,
                verticalVelocity,
                rotationVelocity,
                targetRotation,
                input.getInputId()
        );

        producer.sendMovementUpdate(moveResult);
        return moveResult;
    }



    private float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }

    private float smoothDampAngle(float current, float target, float[] velocityRef, float smoothTime, float deltaTime) {
        // Calcula la diferencia entre ángulos (-180 a 180)
        float delta = deltaAngle(current, target);

        // Factor de suavizado (aprox. método de Unity)
        float omega = 2f / smoothTime;
        float x = omega * deltaTime;
        float exp = 1f / (1f + x + 0.48f * x * x + 0.235f * x * x * x);

        float change = delta;
        float temp = (velocityRef[0] + omega * change) * deltaTime;
        velocityRef[0] = (velocityRef[0] - omega * temp) * exp;

        float result = current + (change + temp) * exp;

        // Asegurar que el ángulo final esté en el rango 0-360
        result = (result + 360f) % 360f;

        return result;
    }

    private float deltaAngle(float current, float target) {
        float delta = (target - current) % 360f;
        if (delta > 180f) delta -= 360f;
        if (delta < -180f) delta += 360f;
        return delta;
    }

    private float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }
    private void notifyPositionCorrection(String playerId, ValidatedPosition validatedPos) {
        try {
            Map<String, Object> notification = Map.of(
                    "type", "position_correction",
                    "accepted", validatedPos.isAccepted(),
                    "corrected", validatedPos.isCorrected(),
                    "reason", validatedPos.getReason() != null ? validatedPos.getReason() : "",
                    "position", Map.of(
                            "x", validatedPos.getPosition().getX(),
                            "y", validatedPos.getPosition().getY(),
                            "z", validatedPos.getPosition().getZ()
                    ),
                    "metadata", validatedPos.getMetadata()
            );
            //streamBridge.send("player.position", objectMapper.writeValueAsString(notification));
            //TODO:  Hacer que se notifique a producer.sendUpdate(dto);


        } catch (Exception e) {
            System.out.println("Error notificando corrección: " + e);
        }
    }

}
