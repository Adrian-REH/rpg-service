package com.mmo.service.game_state.infrastructure.messager;

import com.mmo.service.game_state.application.EntityHandlerService;
import com.mmo.service.game_state.application.bridge.PlayerStateBridge;
import com.mmo.service.game_state.application.dto.EntityStateDTO;
import com.mmo.service.game_state.domain.model.MoveInputData;
import jakarta.validation.Valid;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class EntityStateConsumer {

    private final PlayerStateBridge bridge;
    private final EntityStateProducer producer;
    private final EntityHandlerService entityHandlerService;

    public EntityStateConsumer(PlayerStateBridge bridge, EntityStateProducer producer, EntityHandlerService entityHandlerService) {
        this.bridge = bridge;
        this.producer = producer;
        this.entityHandlerService = entityHandlerService;
    }


    @KafkaListener(
            topics = "player.state.move",
            groupId = "player-move",
            containerFactory = "playerMoveInitKafkaListenerContainerFactory"

    )
    public void consumeMovement(@Valid MoveInputData dto) {
        try {
            System.out.println(dto.toString());
            //En caso de necesariase trackear los datos que solicita un usuario al servidor se redirige a WFlux
            bridge.emit(dto);
            entityHandlerService.handleMovement(dto).subscribe(unused -> System.out.println("Movimiento manejado"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "player.state.attack", groupId = "entity-attack")
    public void consumeAttack(@Valid EntityStateDTO dto) {
        try {
            //En caso de necesariase trackear los datos que solicita un usuario al servidor se redirige a WFlux
            if (dto.isActionValid()) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "player.state.takedmg", groupId = "entity-take-damage")
    public void consumeTakeDamage(@Valid EntityStateDTO dto) {
        try {
            //En caso de necesariase trackear los datos que solicita un usuario al servidor se redirige a WFlux
            if (dto.isActionValid()) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "player.state.health", groupId = "entity-health")
    public void consumeHealth(@Valid EntityStateDTO dto) {
        try {
            //En caso de necesariase trackear los datos que solicita un usuario al servidor se redirige a WFlux
            if (dto.isActionValid()) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @KafkaListener(topics = "player.state.respawm", groupId = "entity-spawn")
    public void consumeReSpawn(@Valid EntityStateDTO dto) {
        try {
            //En caso de necesariase trackear los datos que solicita un usuario al servidor se redirige a WFlux
            if (dto.isActionValid()) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "player.state.die", groupId = "entity-die")
    public void consumeDie(@Valid EntityStateDTO dto) {
        try {
            //En caso de necesariase trackear los datos que solicita un usuario al servidor se redirige a WFlux
            if (dto.isActionValid()) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
