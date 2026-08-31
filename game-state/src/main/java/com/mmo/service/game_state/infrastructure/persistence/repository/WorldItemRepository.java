package com.mmo.service.game_state.infrastructure.persistence.repository;

import com.mmo.service.game_state.infrastructure.persistence.entity.WorldItemEntity;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface WorldItemRepository extends R2dbcRepository<WorldItemEntity, String> {

    @Query("SELECT * FROM world_items " +
            "WHERE zone_id = :zoneId " +
            "AND is_available = 'Y' " +
            "AND pos_x BETWEEN :minX AND :maxX " +
            "AND pos_z BETWEEN :minZ AND :maxZ")
    Flux<WorldItemEntity> findByZoneAndBounds(
            String zoneId, Double minX, Double maxX, Double minZ, Double maxZ
    );

    @Query("SELECT * FROM world_items " +
            "WHERE zone_id = :zoneId " +
            "AND is_available = 'Y' " +
            "AND SQRT(POWER(pos_x - :x, 2) + POWER(pos_z - :z, 2)) <= :radius")
    Flux<WorldItemEntity> findByZoneAndRadius(
            String zoneId, Double x, Double z, Double radius
    );

    Flux<WorldItemEntity> findByZoneIdAndIsAvailable(String zoneId, Boolean isAvailable);

    @Modifying
    @Query("UPDATE world_items SET is_available = 'N' WHERE world_item_id = :id")
    Mono<Integer> markAsPickedUp(String id);
}
