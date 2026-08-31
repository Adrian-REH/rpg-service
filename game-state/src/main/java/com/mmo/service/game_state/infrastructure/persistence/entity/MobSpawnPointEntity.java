package com.mmo.service.game_state.infrastructure.persistence.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("mob_spawn_points")
public class MobSpawnPointEntity {
    @Id
    private String spawnId;
    private String mobTemplateId;
    private String zoneId;
    private Double posX;
    private Double posY;
    private Double posZ;
    private Double spawnRadius;
    private Integer maxMobs;
    private Integer respawnTime;
    private Boolean isActive;
    private String patrolPath; // JSON

    public MobSpawnPointEntity() {
    }

    public MobSpawnPointEntity(String spawnId, String mobTemplateId, String zoneId, Double posX, Double posY, Double posZ, Double spawnRadius, Integer maxMobs, Integer respawnTime, Boolean isActive, String patrolPath) {
        this.spawnId = spawnId;
        this.mobTemplateId = mobTemplateId;
        this.zoneId = zoneId;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.spawnRadius = spawnRadius;
        this.maxMobs = maxMobs;
        this.respawnTime = respawnTime;
        this.isActive = isActive;
        this.patrolPath = patrolPath;
    }

    public String getSpawnId() {
        return spawnId;
    }

    public void setSpawnId(String spawnId) {
        this.spawnId = spawnId;
    }

    public String getMobTemplateId() {
        return mobTemplateId;
    }

    public void setMobTemplateId(String mobTemplateId) {
        this.mobTemplateId = mobTemplateId;
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

    public Double getSpawnRadius() {
        return spawnRadius;
    }

    public void setSpawnRadius(Double spawnRadius) {
        this.spawnRadius = spawnRadius;
    }

    public Integer getMaxMobs() {
        return maxMobs;
    }

    public void setMaxMobs(Integer maxMobs) {
        this.maxMobs = maxMobs;
    }

    public Integer getRespawnTime() {
        return respawnTime;
    }

    public void setRespawnTime(Integer respawnTime) {
        this.respawnTime = respawnTime;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getPatrolPath() {
        return patrolPath;
    }

    public void setPatrolPath(String patrolPath) {
        this.patrolPath = patrolPath;
    }
}
