package com.mmo.service.gateway.application.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig  {



    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity  http, JwtReactiveAuthenticationManager jwtAuthManager) {
        http.csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(authorizeExchangeSpec -> authorizeExchangeSpec
                        /*.pathMatchers("").access( (monoAuth, context) ->
                                monoAuth.flatMap(authentication -> {
                                    boolean allowed = authentication.isAuthenticated();
                                    return Mono.just(new AuthorizationDecision(allowed));
                                })
                        )*/
                        .pathMatchers("/ws/**").permitAll()
                        .anyExchange().permitAll()
                )
                /*.authenticationManager(jwtAuthManager)*/
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable);
        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}


