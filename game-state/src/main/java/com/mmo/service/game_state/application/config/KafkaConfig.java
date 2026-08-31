package com.mmo.service.game_state.application.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmo.service.game_state.application.dto.WorldInitDTO;
import com.mmo.service.game_state.application.dto.PlayerDTO;
import com.mmo.service.game_state.domain.model.MoveInputData;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    // ============================================================
    // 🧍 PLAYER DTO CONSUMER
    // ============================================================
    @Bean
    public ConsumerFactory<String, PlayerDTO> playerConsumerFactory() {
        JsonDeserializer<PlayerDTO> deserializer = new JsonDeserializer<>(PlayerDTO.class);
        deserializer.addTrustedPackages("*");

        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "player-group");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(
                config,
                new StringDeserializer(),
                deserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PlayerDTO>
    playerKafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, PlayerDTO> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(playerConsumerFactory());
        return factory;
    }


    // ============================================================
    // 🌍 ENTITY STATE DTO CONSUMER
    // ============================================================
    @Bean
    public ConsumerFactory<String, WorldInitDTO> entityInitConsumerFactory() {
        JsonDeserializer<WorldInitDTO> deserializer = new JsonDeserializer<>(WorldInitDTO.class);
        deserializer.addTrustedPackages("*");

        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "world-state");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(
                config,
                new StringDeserializer(),
                deserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, WorldInitDTO>
    worldInitKafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, WorldInitDTO> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(entityInitConsumerFactory());
        return factory;
    }



    // ============================================================
    // 🌍 ENTITY STATE DTO CONSUMER
    // ============================================================
    @Bean
    public ConsumerFactory<String, MoveInputData> playerMoveInitConsumerFactory() {
        JsonDeserializer<MoveInputData> deserializer = new JsonDeserializer<>(MoveInputData.class);
        deserializer.addTrustedPackages("*");

        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, "player-move");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(
                config,
                new StringDeserializer(),
                deserializer
        );
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, MoveInputData>
    playerMoveInitKafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, MoveInputData> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(playerMoveInitConsumerFactory());
        return factory;
    }

    @Bean
    public ProducerFactory<String, String> producerFactory(ObjectMapper objectMapper) {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(JsonSerializer.ADD_TYPE_INFO_HEADERS, false);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate(ObjectMapper objectMapper) {
        return new KafkaTemplate<>(producerFactory(objectMapper));
    }


}
