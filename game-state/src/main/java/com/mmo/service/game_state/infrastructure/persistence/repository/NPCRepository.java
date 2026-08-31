package com.mmo.service.game_state.infrastructure.persistence.repository;

import com.mmo.service.game_state.infrastructure.persistence.entity.NPCEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface NPCRepository extends R2dbcRepository<NPCEntity, String> {
    Flux<NPCEntity> findByZoneIdAndIsActive(String zoneId, Boolean isActive);

    @Query("SELECT * FROM npcs " +
            "WHERE zone_id = :zoneId " +
            "AND is_active = 1 " +
            "AND SQRT(POWER(pos_x - :x, 2) + POWER(pos_z - :z, 2)) <= :radius")
    Flux<NPCEntity> findByZoneAndRadius(
            String zoneId, Double x, Double z, Double radius
    );
}
