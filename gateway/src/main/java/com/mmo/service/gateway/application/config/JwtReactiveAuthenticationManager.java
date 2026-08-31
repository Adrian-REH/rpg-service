package com.mmo.service.gateway.application.config;

import com.mmo.service.gateway.application.JwtService;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class JwtReactiveAuthenticationManager implements ReactiveAuthenticationManager {
    private final JwtService jwtService;

    public JwtReactiveAuthenticationManager(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = authentication.getCredentials().toString();
        if (jwtService.isValid(token)) {
            return Mono.just(new UsernamePasswordAuthenticationToken(
                    jwtService.getPlayerIdFromToken(token),
                    null,
                    jwtService.getAuthorities(token)
            ));
        }
        return Mono.empty();
    }
}
