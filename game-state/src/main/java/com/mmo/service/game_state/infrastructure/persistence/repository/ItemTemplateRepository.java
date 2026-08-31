package com.mmo.service.game_state.infrastructure.persistence.repository;

import com.mmo.service.game_state.infrastructure.persistence.entity.ItemTemplateEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ItemTemplateRepository extends R2dbcRepository<ItemTemplateEntity, String> {
    Flux<ItemTemplateEntity> findByItemType(String itemType);

    Flux<ItemTemplateEntity> findByRarity(String rarity);
}
