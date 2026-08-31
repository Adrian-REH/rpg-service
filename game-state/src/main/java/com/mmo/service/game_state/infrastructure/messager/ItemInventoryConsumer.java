package com.mmo.service.game_state.infrastructure.messager;

import com.mmo.service.game_state.application.ItemInteractionService;
import com.mmo.service.game_state.application.dto.EntityStateDTO;
import com.mmo.service.game_state.application.dto.ItemStateDTO;
import jakarta.validation.Valid;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ItemInventoryConsumer {

    private final ItemInteractionService service;

    public ItemInventoryConsumer( ItemInteractionService service) {
        this.service = service;
    }


    @KafkaListener(topics = "player.item")
    public void consumeItemState(@Valid ItemStateDTO dto) {
        try {
            if (dto.getAction().isPickUp()) service.pickupItem(dto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
