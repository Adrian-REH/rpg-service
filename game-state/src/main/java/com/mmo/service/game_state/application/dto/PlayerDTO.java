package com.mmo.service.game_state.application.dto;


import com.mmo.service.game_state.infrastructure.persistence.entity.PlayerEntity;

import java.time.LocalDateTime;

public class PlayerDTO {
    private String type;
    private String zoneId;
    private String playerId;
    private String username;
    private Double posX;
    private Double posY;
    private Double posZ;
    private Integer level;
    private Long experience;
    private Integer currentHp;
    private Integer maxHp;
    private Integer currentMp;
    private Integer maxMp;
    private Long gold;
    private Boolean isOnline;
    private LocalDateTime lastLogin;
    private LocalDateTime createdAt;

    // Constructor vacío
    public PlayerDTO() {}

    // Constructor desde PlayerEntity
    public PlayerDTO(PlayerEntity entity) {
        this.playerId = entity.getPlayerId();
        this.username = entity.getUsername();
        this.zoneId = entity.getCurrentZoneId();
        this.posX = entity.getPosX();
        this.posY = entity.getPosY();
        this.posZ = entity.getPosZ();
        this.level = entity.getLevel();
        this.experience = entity.getExperience();
        this.currentHp = entity.getCurrentHp();
        this.maxHp = entity.getMaxHp();
        this.currentMp = entity.getCurrentMp();
        this.maxMp = entity.getMaxMp();
        this.gold = entity.getGold();
        this.isOnline = entity.getIsOnline();
        this.lastLogin = entity.getLastLogin();
        this.createdAt = entity.getCreatedAt();
        this.type = "player_init_data";
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public Boolean getOnline() {
        return isOnline;
    }

    public void setOnline(Boolean online) {
        isOnline = online;
    }

    // Getters y Setters
    public String getPlayerId() { return playerId; }
    public void setPlayerId(String playerId) { this.playerId = playerId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }


    public Double getPosX() { return posX; }
    public void setPosX(Double posX) { this.posX = posX; }

    public Double getPosY() { return posY; }
    public void setPosY(Double posY) { this.posY = posY; }

    public Double getPosZ() { return posZ; }
    public void setPosZ(Double posZ) { this.posZ = posZ; }

    public Integer getLevel() { return level; }
    public void setLevel(Integer level) { this.level = level; }

    public Long getExperience() { return experience; }
    public void setExperience(Long experience) { this.experience = experience; }

    public Integer getCurrentHp() { return currentHp; }
    public void setCurrentHp(Integer currentHp) { this.currentHp = currentHp; }

    public Integer getMaxHp() { return maxHp; }
    public void setMaxHp(Integer maxHp) { this.maxHp = maxHp; }

    public Integer getCurrentMp() { return currentMp; }
    public void setCurrentMp(Integer currentMp) { this.currentMp = currentMp; }

    public Integer getMaxMp() { return maxMp; }
    public void setMaxMp(Integer maxMp) { this.maxMp = maxMp; }

    public Long getGold() { return gold; }
    public void setGold(Long gold) { this.gold = gold; }

    public Boolean getIsOnline() { return isOnline; }
    public void setIsOnline(Boolean isOnline) { this.isOnline = isOnline; }

    public LocalDateTime getLastLogin() { return lastLogin; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
