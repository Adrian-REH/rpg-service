package com.mmo.service.game_state.infrastructure.messager;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ItemInventoryProducer {
    private final StreamBridge streamBridge;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public ItemInventoryProducer(StreamBridge streamBridge, KafkaTemplate<String, String> kafkaTemplate) {
        this.streamBridge = streamBridge;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendUpdate(String stateDTO) {
        kafkaTemplate.send("player.item", stateDTO);
    }
}
