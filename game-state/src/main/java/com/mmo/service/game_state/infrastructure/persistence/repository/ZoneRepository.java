package com.mmo.service.game_state.infrastructure.persistence.repository;

import com.mmo.service.game_state.infrastructure.persistence.entity.ZoneEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ZoneRepository extends R2dbcRepository<ZoneEntity, String> {
    Flux<ZoneEntity> findByMinLevelLessThanEqual(Integer level);
}
