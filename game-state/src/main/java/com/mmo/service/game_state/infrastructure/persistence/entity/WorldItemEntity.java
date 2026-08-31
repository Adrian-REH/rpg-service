package com.mmo.service.game_state.infrastructure.persistence.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("world_items")
public class WorldItemEntity {
    @Id
    private String worldItemId;
    private String itemTemplateId;
    private String zoneId;
    private Double posX;
    private Double posY;
    private Double posZ;
    private Integer quantity;
    private LocalDateTime spawnedAt;
    private LocalDateTime despawnAt;
    private Character isAvailable;
    private String droppedBy;

    public WorldItemEntity() {
    }

    public WorldItemEntity(String worldItemId, String itemTemplateId, String zoneId, Double posX, Double posY, Double posZ, Integer quantity, LocalDateTime spawnedAt, LocalDateTime despawnAt, Character isAvailable, String droppedBy) {
        this.worldItemId = worldItemId;
        this.itemTemplateId = itemTemplateId;
        this.zoneId = zoneId;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.quantity = quantity;
        this.spawnedAt = spawnedAt;
        this.despawnAt = despawnAt;
        this.isAvailable = isAvailable;
        this.droppedBy = droppedBy;
    }

    public String getWorldItemId() {
        return worldItemId;
    }

    public void setWorldItemId(String worldItemId) {
        this.worldItemId = worldItemId;
    }

    public String getItemTemplateId() {
        return itemTemplateId;
    }

    public void setItemTemplateId(String itemTemplateId) {
        this.itemTemplateId = itemTemplateId;
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

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getSpawnedAt() {
        return spawnedAt;
    }

    public void setSpawnedAt(LocalDateTime spawnedAt) {
        this.spawnedAt = spawnedAt;
    }

    public LocalDateTime getDespawnAt() {
        return despawnAt;
    }

    public void setDespawnAt(LocalDateTime despawnAt) {
        this.despawnAt = despawnAt;
    }

    public Boolean getIsAvailable() {
        return isAvailable == 'Y';
    }

    public void setIsAvailable(Boolean available) {
        isAvailable = available? 'Y':'N';
    }

    public String getDroppedBy() {
        return droppedBy;
    }

    public void setDroppedBy(String droppedBy) {
        this.droppedBy = droppedBy;
    }
}
