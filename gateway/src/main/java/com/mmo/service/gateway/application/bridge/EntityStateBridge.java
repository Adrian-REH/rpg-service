package com.mmo.service.gateway.application.bridge;

import com.mmo.service.gateway.application.dto.EntityStateDTO;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Service
public class EntityStateBridge {
    private final Sinks.One<EntityStateDTO> sink = Sinks.one();
    public void emit(EntityStateDTO dto) {
        sink.tryEmitValue(dto);
    }
    public Mono<EntityStateDTO> getMono() {
        return sink.asMono();
    }
}
