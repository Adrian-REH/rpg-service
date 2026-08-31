package com.mmo.service.game_state.infrastructure.persistence.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("item_templates")
public class ItemTemplateEntity {
    @Id
    private String itemTemplateId;
    private String itemName;
    private String itemType;
    private String rarity;
    private Integer levelRequired;
    private Integer stackSize;
    private String iconUrl;
    private String description;
    private Long baseValue;
    private String metadata; // JSON
    private LocalDateTime createdAt;

    public ItemTemplateEntity() {
    }

    public ItemTemplateEntity(String itemTemplateId, String itemName, String itemType, String rarity, Integer levelRequired, Integer stackSize, String iconUrl, String description, Long baseValue, String metadata, LocalDateTime createdAt) {
        this.itemTemplateId = itemTemplateId;
        this.itemName = itemName;
        this.itemType = itemType;
        this.rarity = rarity;
        this.levelRequired = levelRequired;
        this.stackSize = stackSize;
        this.iconUrl = iconUrl;
        this.description = description;
        this.baseValue = baseValue;
        this.metadata = metadata;
        this.createdAt = createdAt;
    }

    public String getItemTemplateId() {
        return itemTemplateId;
    }

    public void setItemTemplateId(String itemTemplateId) {
        this.itemTemplateId = itemTemplateId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getRarity() {
        return rarity;
    }

    public void setRarity(String rarity) {
        this.rarity = rarity;
    }

    public Integer getLevelRequired() {
        return levelRequired;
    }

    public void setLevelRequired(Integer levelRequired) {
        this.levelRequired = levelRequired;
    }

    public Integer getStackSize() {
        return stackSize;
    }

    public void setStackSize(Integer stackSize) {
        this.stackSize = stackSize;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getBaseValue() {
        return baseValue;
    }

    public void setBaseValue(Long baseValue) {
        this.baseValue = baseValue;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
