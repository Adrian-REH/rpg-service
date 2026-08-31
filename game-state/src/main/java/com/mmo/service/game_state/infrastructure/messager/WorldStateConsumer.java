package com.mmo.service.game_state.infrastructure.messager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmo.service.game_state.application.GameWorldService;
import com.mmo.service.game_state.application.WorldInitializationService;
import com.mmo.service.game_state.application.dto.WorldInitDTO;
import jakarta.validation.Valid;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class WorldStateConsumer {
    private final GameWorldService service;
    private final WorldInitializationService worldInitializationService;
    private final ObjectMapper objectMapper;
    private final ObjectMapper mapper = new ObjectMapper();

    public WorldStateConsumer(GameWorldService service, WorldInitializationService worldInitializationService, ObjectMapper objectMapper) {
        this.service = service;
        this.worldInitializationService = worldInitializationService;
        this.objectMapper = objectMapper;
    }
    @KafkaListener(
            topics = "world.state",
            groupId = "world-state",
            containerFactory = "worldInitKafkaListenerContainerFactory"
    )
    public void consumeInitWorld(@Valid WorldInitDTO record) {
        try {
            System.out.println(record.toString());
            //En caso de necesariase trackear los datos que solicita un usuario al servidor se redirige a WFlux
            worldInitializationService.initializePlayerAndWorld(record);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
