package com.mmo.service.gateway.application;

import com.mmo.service.gateway.application.dto.LoginDTO;
import com.mmo.service.gateway.infrastructure.persistence.repository.PlayerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class Login {
    private final PlayerRepository playerRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public Login(PlayerRepository playerRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.playerRepository = playerRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public Mono<Map<String, String>> execute(LoginDTO dto) {
        return playerRepository.findByUsername(dto.getUsername())
                .flatMap(playerEntity -> {
                    if (passwordEncoder.matches(dto.getPassword(), playerEntity.getPasswordHash())){
                        String jwt = jwtService.generateJwt(playerEntity.getPlayerId());
                        Map<String, String> body = Map.of("Token", jwt ,"playerId",playerEntity.getPlayerId());
                        return Mono.just(body);
                    } else {
                        return Mono.empty();
                    }
                });
    }
}
