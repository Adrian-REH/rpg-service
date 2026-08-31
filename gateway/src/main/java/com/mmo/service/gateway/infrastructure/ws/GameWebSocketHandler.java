package com.mmo.service.gateway.infrastructure.ws;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmo.service.gateway.application.JwtService;
import com.mmo.service.gateway.application.config.JwtReactiveAuthenticationManager;
import com.mmo.service.gateway.application.dto.EntityStateDTO;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.*;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class GameWebSocketHandler implements WebSocketHandler  {

    private final JwtReactiveAuthenticationManager jwtReactiveAuthenticationManager;
    private final JwtService jwtService;
    private static class SessionInfo {
        final WebSocketSession session;
        final Sinks.Many<String> sink;

        SessionInfo(WebSocketSession session, Sinks.Many<String> sink) {
            this.session = session;
            this.sink = sink;
        }
    }
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    private final Map<String, SessionInfo> sessions = new ConcurrentHashMap<>();

    public GameWebSocketHandler(JwtReactiveAuthenticationManager jwtReactiveAuthenticationManager, JwtService jwtService, KafkaTemplate<String, String> kafkaTemplate) {
        this.jwtReactiveAuthenticationManager = jwtReactiveAuthenticationManager;
        this.jwtService = jwtService;
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = new ObjectMapper();
    }


    @Override
    public Mono<Void> handle(WebSocketSession session) {
        String sessionId = session.getId();
        Sinks.Many<String> sink = Sinks.many().unicast().onBackpressureBuffer();

        // Extraer token de query param
        String query = session.getHandshakeInfo().getUri().getQuery();
        String jwt = Arrays.stream(Optional.ofNullable(query).orElse("").split("&"))
                .filter(s -> s.startsWith("token="))
                .map(s -> s.substring("token=".length()))
                .findFirst()
                .orElse(null);

        if (jwt == null) {
            return session.close(CloseStatus.NOT_ACCEPTABLE.withReason("JWT missing"));
        }
        String entityId = jwtService.getPlayerIdFromToken(jwt);
        // Encadenar la autenticación
        return jwtReactiveAuthenticationManager
                .authenticate(new BearerTokenAuthenticationToken(jwt))
                .flatMap(auth -> {
                    System.out.println("✅ Autenticación correcta: " + auth.getName());
                    if (!sessions.containsKey(entityId)) sessions.putIfAbsent(entityId, new SessionInfo(session, sink));

                    // Flujo de entrada
                    Mono<Void> input = session.receive()
                            .doOnSubscribe(sub -> System.out.println("Cliente conectado: " + sessionId))
                            .map(WebSocketMessage::getPayloadAsText)
                            .flatMap(message -> Mono.fromCallable(() -> {
                                EntityStateDTO dto = objectMapper.readValue(message, EntityStateDTO.class);
                                if (dto.getType().equals("player_move")) {
                                    kafkaTemplate.send("player.state.move", dto.getEntityId(), message);
                                } else if (dto.getType().equals("world_init")){
                                    System.out.println(dto.toString());
                                    kafkaTemplate.send("world.state", dto.getEntityId(), message);
                                }
                                return dto;
                            }).onErrorResume(e -> {
                                System.err.println("Error procesando mensaje: " + e.getMessage());
                                e.printStackTrace();
                                return Mono.empty();
                            }))
                            .doOnError(error -> System.err.println("Error en WS: " + error.getMessage()))
                            .doFinally(signal -> {
                                System.out.println("Conexion cerrada (input): " + sessionId + " - " + signal);
                                sessions.values().removeIf(info -> info.session.equals(session));
                            })
                            .then();

                    // Flujo de salida
                    Mono<Void> output = session.send(
                            sink.asFlux().map(session::textMessage)
                    );

                    return Mono.zip(input, output)
                            .doFinally(signal -> {
                                System.out.println("Conexion cerrada (output): " + sessionId + " - " + signal);
                                sessions.values().removeIf(info -> info.session.equals(session));
                                sink.tryEmitComplete();
                            }).then();
                })
                .onErrorResume(err -> {
                    // JWT inválido o error de autenticación
                    System.err.println("❌ JWT inválido o expirado: " + err.getMessage());
                    return session.close(CloseStatus.NOT_ACCEPTABLE.withReason("JWT inválido o expirado"));
                });
    }

    public void sendToPlayer(String playerId, String dto) {
        SessionInfo sessionInfo = sessions.get(playerId);
        if (sessionInfo != null && sessionInfo.session.isOpen()) {
            Sinks.EmitResult result = sessionInfo.sink.tryEmitNext(dto);
            if (result.isFailure()) {

                switch (result){
                    case FAIL_OVERFLOW -> System.err.println("Buffer lleno para " + playerId + ": " + result);
                    case FAIL_TERMINATED, FAIL_CANCELLED -> {
                        System.err.println("Sesion terminada para: " + playerId);
                        sessions.remove(playerId);
                    }
                    default -> System.err.println("Error inesperado: " + result);
                }
            }
        } else {
            System.out.println("Sesión no encontrada o cerrada para entityId: " + playerId);
            sessions.remove(playerId);
        }
    }
}
