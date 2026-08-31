package com.mmo.service.game_state.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class WorldInitDTO {

    @NotNull(message = "type es obligatorio")
    private String type;
    @NotBlank(message = "entityId no puede estar vacío")
    private String entityId;    // UUID o ID único
    public WorldInitDTO() {}

    public WorldInitDTO(String type, String entityId) {
        this.type = type;
        this.entityId = entityId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    @Override
    public String toString() {
        return "EntityInitDTO{" +
                "type='" + type + '\'' +
                ", entityId='" + entityId + '\'' +
                '}';
    }
}
