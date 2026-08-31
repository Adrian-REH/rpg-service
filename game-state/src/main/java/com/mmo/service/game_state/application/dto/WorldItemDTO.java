package com.mmo.service.game_state.application.dto;

import com.mmo.service.game_state.infrastructure.persistence.entity.WorldItemEntity;

import java.time.LocalDateTime;

public class WorldItemDTO {
    private String worldItemId;
    private String itemTemplateId;
    private String zoneId;
    private Double posX;
    private Double posY;
    private Double posZ;
    private Integer quantity;
    private LocalDateTime spawnedAt;
    private LocalDateTime despawnAt;
    private Boolean isAvailable;
    private String droppedBy;
    private String type;  // Nuevo campo

    // Constructor vacío (para deserialización)
    public WorldItemDTO() {}

    // Constructor desde la entidad
    public WorldItemDTO(WorldItemEntity entity) {
        this.worldItemId = entity.getWorldItemId();
        this.itemTemplateId = entity.getItemTemplateId();
        this.zoneId = entity.getZoneId();
        this.posX = entity.getPosX();
        this.posY = entity.getPosY();
        this.posZ = entity.getPosZ();
        this.quantity = entity.getQuantity();
        this.spawnedAt = entity.getSpawnedAt();
        this.despawnAt = entity.getDespawnAt();
        this.isAvailable = entity.getIsAvailable();
        this.droppedBy = entity.getDroppedBy();
        this.type = "world_item_update";
    }

    // Getters y Setters
    public String getWorldItemId() { return worldItemId; }
    public void setWorldItemId(String worldItemId) { this.worldItemId = worldItemId; }

    public String getItemTemplateId() { return itemTemplateId; }
    public void setItemTemplateId(String itemTemplateId) { this.itemTemplateId = itemTemplateId; }

    public String getZoneId() { return zoneId; }
    public void setZoneId(String zoneId) { this.zoneId = zoneId; }

    public Double getPosX() { return posX; }
    public void setPosX(Double posX) { this.posX = posX; }

    public Double getPosY() { return posY; }
    public void setPosY(Double posY) { this.posY = posY; }

    public Double getPosZ() { return posZ; }
    public void setPosZ(Double posZ) { this.posZ = posZ; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public LocalDateTime getSpawnedAt() { return spawnedAt; }
    public void setSpawnedAt(LocalDateTime spawnedAt) { this.spawnedAt = spawnedAt; }

    public LocalDateTime getDespawnAt() { return despawnAt; }
    public void setDespawnAt(LocalDateTime despawnAt) { this.despawnAt = despawnAt; }

    public Boolean getIsAvailable() { return isAvailable; }
    public void setIsAvailable(Boolean isAvailable) { this.isAvailable = isAvailable; }

    public String getDroppedBy() { return droppedBy; }
    public void setDroppedBy(String droppedBy) { this.droppedBy = droppedBy; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
