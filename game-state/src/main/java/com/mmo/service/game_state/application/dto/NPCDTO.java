package com.mmo.service.game_state.application.dto;

import com.mmo.service.game_state.infrastructure.persistence.entity.NPCEntity;

import java.time.LocalDateTime;

public class NPCDTO {
    private String npcId;
    private String npcName;
    private String npcType;
    private String zoneId;
    private Double posX;
    private Double posY;
    private Double posZ;
    private Double rotY;
    private String dialogueTree; // JSON
    private String vendorItems;  // JSON
    private Boolean isActive;
    private Integer respawnTime;
    private String type;         // Nuevo campo

    // Constructor vacío para deserialización
    public NPCDTO() {}

    // Constructor desde la entidad NPC
    public NPCDTO(NPCEntity entity) {
        this.npcId = entity.getNpcId();
        this.npcName = entity.getNpcName();
        this.npcType = entity.getNpcType();
        this.zoneId = entity.getZoneId();
        this.posX = entity.getPosX();
        this.posY = entity.getPosY();
        this.posZ = entity.getPosZ();
        this.rotY = entity.getRotY();
        this.dialogueTree = entity.getDialogueTree();
        this.vendorItems = entity.getVendorItems();
        this.isActive = entity.getActive();
        this.respawnTime = entity.getRespawnTime();
        this.type = "world_npc_update"; // O el valor que quieras
    }

    // Getters y Setters
    public String getNpcId() { return npcId; }
    public void setNpcId(String npcId) { this.npcId = npcId; }

    public String getNpcName() { return npcName; }
    public void setNpcName(String npcName) { this.npcName = npcName; }

    public String getNpcType() { return npcType; }
    public void setNpcType(String npcType) { this.npcType = npcType; }

    public String getZoneId() { return zoneId; }
    public void setZoneId(String zoneId) { this.zoneId = zoneId; }

    public Double getPosX() { return posX; }
    public void setPosX(Double posX) { this.posX = posX; }

    public Double getPosY() { return posY; }
    public void setPosY(Double posY) { this.posY = posY; }

    public Double getPosZ() { return posZ; }
    public void setPosZ(Double posZ) { this.posZ = posZ; }

    public Double getRotY() { return rotY; }
    public void setRotY(Double rotY) { this.rotY = rotY; }

    public String getDialogueTree() { return dialogueTree; }
    public void setDialogueTree(String dialogueTree) { this.dialogueTree = dialogueTree; }

    public String getVendorItems() { return vendorItems; }
    public void setVendorItems(String vendorItems) { this.vendorItems = vendorItems; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public Integer getRespawnTime() { return respawnTime; }
    public void setRespawnTime(Integer respawnTime) { this.respawnTime = respawnTime; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
