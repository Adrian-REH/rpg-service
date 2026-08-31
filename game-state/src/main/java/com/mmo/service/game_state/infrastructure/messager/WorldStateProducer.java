package com.mmo.service.game_state.infrastructure.messager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.mmo.service.game_state.application.dto.MobDTO;
import com.mmo.service.game_state.application.dto.NPCDTO;
import com.mmo.service.game_state.application.dto.WorldItemDTO;
import com.mmo.service.game_state.infrastructure.persistence.entity.ActiveMobEntity;
import com.mmo.service.game_state.infrastructure.persistence.entity.NPCEntity;
import com.mmo.service.game_state.infrastructure.persistence.entity.WorldItemEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WorldStateProducer {
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;


    public WorldStateProducer(ObjectMapper objectMapper, KafkaTemplate<String, String> kafkaTemplate) {
        this.objectMapper = objectMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendItemUpdate(String entityId, WorldItemDTO dto) throws JsonProcessingException {
        System.out.println("sendItemUpdate");
        dto.setType("world_item_update");
        String json = objectMapper.writeValueAsString(dto);
        kafkaTemplate.send("world.update", entityId, json);
    }

    public void sendItemUpdate(String entityId, List<WorldItemDTO> dto) throws JsonProcessingException {
        System.out.println("sendItemUpdate");
        String json = objectMapper.writeValueAsString(dto);
        kafkaTemplate.send("world.update", entityId, json);
    }

    public void sendNPCUpdate(String entityId, NPCDTO mob) throws JsonProcessingException {
        System.out.println("sendNPCUpdate");
        mob.setType("world_npc_update");
        String json = objectMapper.writeValueAsString(mob);
        kafkaTemplate.send("world.update", entityId, json);
    }
    public void sendNPCUpdate(String entityId, List<NPCDTO> mob) throws JsonProcessingException {
        System.out.println("sendNPCUpdate");
        String json = objectMapper.writeValueAsString(mob);
        kafkaTemplate.send("world.update", entityId, json);
    }

    public void sendMobUpdate(String entityId, MobDTO mob) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(mob);
        System.out.println("sendMobUpdate");
        mob.setType("world_mob_update");
        kafkaTemplate.send("world.update", entityId, json);
    }

    public void sendMobUpdate(String entityId, List<MobDTO> mob) throws JsonProcessingException {
        String json = objectMapper.writeValueAsString(mob);
        System.out.println("sendMobUpdate");
        kafkaTemplate.send("world.update", entityId, json);
    }
}
