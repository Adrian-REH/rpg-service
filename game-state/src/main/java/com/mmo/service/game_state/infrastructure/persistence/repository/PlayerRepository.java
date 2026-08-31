package com.mmo.service.game_state.infrastructure.persistence.repository;

import com.mmo.service.game_state.infrastructure.persistence.entity.PlayerEntity;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface PlayerRepository extends R2dbcRepository<PlayerEntity, String> {

    Mono<PlayerEntity> findByUsername(String username);
    @Modifying
    @Query("UPDATE players SET pos_x = :posX, pos_y = :posY, pos_z = :posZ WHERE player_id = :playerId")
    Mono<Long> updatePositionAndZone(
            String playerId,
            Double posX,
            Double posY,
            Double posZ
    );


    @Modifying
    @Query("UPDATE players SET is_online = :status WHERE player_id = :playerId")
    Mono<PlayerEntity> updateOnlineStatus(String playerId, char status);

}
