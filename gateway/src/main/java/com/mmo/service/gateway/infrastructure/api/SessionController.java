package com.mmo.service.gateway.infrastructure.api;

import com.mmo.service.gateway.application.Login;
import com.mmo.service.gateway.application.dto.LoginDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
public class SessionController {
    private final Login login;

    public SessionController(Login login) {
        this.login = login;
    }


    @PostMapping("/login")
    public Mono<ResponseEntity<Map<String, String>>> login(@RequestBody LoginDTO dto) {
        return login.execute(dto).flatMap(data->{
            if (!data.isEmpty()) return Mono.just(ResponseEntity.ok(data));
            else return Mono.just(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
        });
    }
}
