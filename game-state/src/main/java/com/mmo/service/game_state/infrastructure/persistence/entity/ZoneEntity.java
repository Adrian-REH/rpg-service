package com.mmo.service.game_state.infrastructure.persistence.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("zones")
public class ZoneEntity {
    @Id
    private String zoneId;
    private String zoneName;
    private Double minX;
    private Double maxX;
    private Double minZ;
    private Double maxZ;
    private Integer minLevel;
    private Integer maxLevel;
    private Boolean isSafeZone;
    private LocalDateTime createdAt;

    public ZoneEntity(String zoneId, String zoneName, Double minX, Double maxX, Double minZ, Double maxZ, Integer minLevel, Integer maxLevel, Boolean isSafeZone, LocalDateTime createdAt) {
        this.zoneId = zoneId;
        this.zoneName = zoneName;
        this.minX = minX;
        this.maxX = maxX;
        this.minZ = minZ;
        this.maxZ = maxZ;
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
        this.isSafeZone = isSafeZone;
        this.createdAt = createdAt;
    }

    public ZoneEntity() {
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public Double getMinX() {
        return minX;
    }

    public void setMinX(Double minX) {
        this.minX = minX;
    }

    public Double getMaxX() {
        return maxX;
    }

    public void setMaxX(Double maxX) {
        this.maxX = maxX;
    }

    public Double getMinZ() {
        return minZ;
    }

    public void setMinZ(Double minZ) {
        this.minZ = minZ;
    }

    public Double getMaxZ() {
        return maxZ;
    }

    public void setMaxZ(Double maxZ) {
        this.maxZ = maxZ;
    }

    public Integer getMinLevel() {
        return minLevel;
    }

    public void setMinLevel(Integer minLevel) {
        this.minLevel = minLevel;
    }

    public Integer getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(Integer maxLevel) {
        this.maxLevel = maxLevel;
    }

    public Boolean getSafeZone() {
        return isSafeZone;
    }

    public void setSafeZone(Boolean safeZone) {
        isSafeZone = safeZone;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
