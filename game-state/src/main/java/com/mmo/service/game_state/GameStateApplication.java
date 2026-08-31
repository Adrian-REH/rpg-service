package com.mmo.service.game_state;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class GameStateApplication {

	public static void main(String[] args) {
		SpringApplication.run(GameStateApplication.class, args);
	}

}
