package com.mmo.service.game_state.infrastructure.persistence.entity;

// =============== ENTITIES ===============


import org.springframework.data.annotation.Id;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;


@Table("active_mobs")
public class ActiveMobEntity {
    @Id
    private String mobId;
    private String mobTemplateId;
    private String spawnId;
    private String zoneId;
    private Double posX;
    private Double posY;
    private Double posZ;
    private Double rotY;
    private Integer currentHp;
    private Integer maxHp;
    private String state;
    private String targetEntityId;
    private LocalDateTime spawnedAt;
    private LocalDateTime lastUpdate;

    public String getMobId() {
        return mobId;
    }

    public void setMobId(String mobId) {
        this.mobId = mobId;
    }

    public String getMobTemplateId() {
        return mobTemplateId;
    }

    public void setMobTemplateId(String mobTemplateId) {
        this.mobTemplateId = mobTemplateId;
    }

    public String getSpawnId() {
        return spawnId;
    }

    public void setSpawnId(String spawnId) {
        this.spawnId = spawnId;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public Double getPosX() {
        return posX;
    }

    public void setPosX(Double posX) {
        this.posX = posX;
    }

    public Double getPosY() {
        return posY;
    }

    public void setPosY(Double posY) {
        this.posY = posY;
    }

    public Double getPosZ() {
        return posZ;
    }

    public void setPosZ(Double posZ) {
        this.posZ = posZ;
    }

    public Double getRotY() {
        return rotY;
    }

    public void setRotY(Double rotY) {
        this.rotY = rotY;
    }

    public Integer getCurrentHp() {
        return currentHp;
    }

    public void setCurrentHp(Integer currentHp) {
        this.currentHp = currentHp;
    }

    public Integer getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(Integer maxHp) {
        this.maxHp = maxHp;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTargetEntityId() {
        return targetEntityId;
    }

    public void setTargetEntityId(String targetEntityId) {
        this.targetEntityId = targetEntityId;
    }

    public LocalDateTime getSpawnedAt() {
        return spawnedAt;
    }

    public void setSpawnedAt(LocalDateTime spawnedAt) {
        this.spawnedAt = spawnedAt;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
}

// =============== REPOSITORIES ===============

