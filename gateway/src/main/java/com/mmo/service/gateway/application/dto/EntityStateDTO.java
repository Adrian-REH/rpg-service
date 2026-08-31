package com.mmo.service.gateway.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EntityStateDTO {

    @NotNull(message = "type es obligatorio")
    private String type;
    @NotBlank(message = "entityId no puede estar vacío")
    private String entityId;    // UUID o ID único

    public EntityStateDTO(String type, String entityId) {
        this.type = type;
        this.entityId = entityId;
    }

    public EntityStateDTO() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getEntityId() {
        return entityId;
    }

    @Override
    public String toString() {
        return "EntityStateDTO{" +
                "type='" + type + '\'' +
                ", entityId='" + entityId + '\'' +
                '}';
    }
}
