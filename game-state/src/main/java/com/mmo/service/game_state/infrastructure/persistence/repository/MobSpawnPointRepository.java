package com.mmo.service.game_state.infrastructure.persistence.repository;

import com.mmo.service.game_state.infrastructure.persistence.entity.MobSpawnPointEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface MobSpawnPointRepository extends R2dbcRepository<MobSpawnPointEntity, String> {
    Flux<MobSpawnPointEntity> findByZoneIdAndIsActive(String zoneId, Boolean isActive);
}
