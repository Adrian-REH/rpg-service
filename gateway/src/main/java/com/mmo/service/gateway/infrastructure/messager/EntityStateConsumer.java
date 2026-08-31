package com.mmo.service.gateway.infrastructure.messager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mmo.service.gateway.application.bridge.EntityStateBridge;
import com.mmo.service.gateway.application.dto.EntityStateDTO;
import com.mmo.service.gateway.infrastructure.ws.GameWebSocketHandler;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class EntityStateConsumer {
    private final GameWebSocketHandler webSocketHandler;
    private final ObjectMapper objectMapper;

    public EntityStateConsumer(GameWebSocketHandler webSocketHandler, ObjectMapper objectMapper) {
        this.webSocketHandler = webSocketHandler;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "player.state.data", groupId = "gateway")
    public void consume(EntityStateDTO dto) {
        try {
            System.out.println("Recibido: " + dto.getEntityId());
            String json = objectMapper.writeValueAsString(dto);

            webSocketHandler.sendToPlayer(dto.getEntityId(), json);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "player.update", groupId = "gateway")
    public void consumePlayerUpdate(ConsumerRecord<String, String> record) {
        String entityId = record.key();
        String message = record.value();
        System.out.println("📥 Mensaje recibido:");
        System.out.println("🔑 entityId = " + entityId);
        System.out.println("📦 payload = " + message);
        try {
            String json = objectMapper.writeValueAsString(message);
            webSocketHandler.sendToPlayer(entityId, json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = "world.update", groupId = "gateway")
    public void consumeWorldUpdate(ConsumerRecord<String, String> record) {
        String entityId = record.key();
        String message = record.value();
        System.out.println("📥 Mensaje recibido:");
        System.out.println("🔑 entityId = " + entityId);
        System.out.println("📦 payload = " + message);

        try {

            String json = objectMapper.writeValueAsString(message);

            webSocketHandler.sendToPlayer(entityId, json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
