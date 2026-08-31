package com.mmo.service.game_state.infrastructure.persistence.repository;

import com.mmo.service.game_state.infrastructure.persistence.entity.ActiveMobEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ActiveMobRepository extends R2dbcRepository<ActiveMobEntity, String> {

    Flux<ActiveMobEntity> findByZoneId(String zoneId);

    @Query("SELECT * FROM active_mobs " +
            "WHERE zone_id = :zoneId " +
            "AND SQRT(POWER(pos_x - :x, 2) + POWER(pos_z - :z, 2)) <= :radius")
    Flux<ActiveMobEntity> findByZoneAndRadius(
            String zoneId, Double x, Double z, Double radius
    );

    Flux<ActiveMobEntity> findByState(String state);

    Mono<Long> countBySpawnId(String spawnId);
}
