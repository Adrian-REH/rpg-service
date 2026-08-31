package com.mmo.service.game_state.application.bridge;


import com.mmo.service.game_state.application.dto.EntityStateDTO;
import com.mmo.service.game_state.domain.model.MoveInputData;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Service
public class PlayerStateBridge {
    private final Sinks.Many<MoveInputData> sink = Sinks.many()
            .multicast()
            .onBackpressureBuffer();
    private final Sinks.One<MoveInputData> sinkMono = Sinks.one();

    public void emit(MoveInputData dto) {
        sink.tryEmitNext(dto);
    }

    public Flux<MoveInputData> getFlux() {
        return sink.asFlux();
    }

    public Mono<MoveInputData> getMono() {
        return sinkMono.asMono();
    }
}
