package com.mmo.service.game_state.infrastructure.persistence.repository;

import com.mmo.service.game_state.infrastructure.persistence.entity.PlayerInventoryEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface PlayerInventoryRepository extends R2dbcRepository<PlayerInventoryEntity, String> {

    Flux<PlayerInventoryEntity> findByPlayerId(String playerId);

    Mono<PlayerInventoryEntity> findByPlayerIdAndItemTemplateId(String playerId, String itemTemplateId);

    @Query("SELECT COALESCE(MIN(slot_num), 0) + 1 AS next_slot " +
            "FROM (SELECT slot_number AS slot_num FROM player_inventory WHERE player_id = :playerId " +
            "      UNION SELECT 0 FROM dual) slots " +
            "WHERE slot_num + 1 NOT IN (SELECT slot_number FROM player_inventory WHERE player_id = :playerId)")
    Mono<Integer> findFirstAvailableSlot(String playerId);

    @Query("DELETE FROM player_inventory WHERE player_id = :playerId AND slot_number = :slot")
    Mono<Void> deleteByPlayerIdAndSlot(String playerId, Integer slot);
}


