package com.mmo.service.game_state.infrastructure.messager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmo.service.game_state.application.dto.EntityStateDTO;
import com.mmo.service.game_state.application.dto.PlayerDTO;
import com.mmo.service.game_state.domain.model.MoveResult;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class EntityStateProducer {
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;


    public EntityStateProducer( ObjectMapper objectMapper, KafkaTemplate<String, String> kafkaTemplate) {
        this.objectMapper = objectMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMovementUpdate(MoveResult result) throws JsonProcessingException {
        System.out.println(result);
        String json = objectMapper.writeValueAsString(result);
        kafkaTemplate.send("player.update", result.getEntityId(), json);

    }
    public void sendEntityUpdate(EntityStateDTO dto) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(dto);
        kafkaTemplate.send("entity.update", dto.getEntityId(), json);
    }
    public void sendPlayerUpdate(PlayerDTO dto) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(dto);
        System.out.println("sendPlayerUpdate");
        kafkaTemplate.send("player.update", dto.getPlayerId(), json);
    }
}
