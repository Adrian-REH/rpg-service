package com.mmo.service.gateway.infrastructure.persistence.repository;

import com.mmo.service.gateway.infrastructure.persistence.entity.PlayerEntity;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface PlayerRepository extends R2dbcRepository<PlayerEntity, String> {
    @Query("SELECT * FROM players WHERE username = :username")
    Mono<PlayerEntity> findByUsername(String username);


    @Modifying
    @Query("UPDATE players SET is_online = :status WHERE player_id = :playerId")
    Mono<PlayerEntity> updateOnlineStatus(String playerId, char status);

}
