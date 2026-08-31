package com.mmo.service.game_state.application.dto;

import jakarta.validation.constraints.*;
import java.util.Map;

public class EntityStateDTO {

    @NotNull(message = "type es obligatorio")
    private String type;
    @NotBlank(message = "entityId no puede estar vacío")
    private String entityId;    // UUID o ID único

    @NotBlank(message = "entityType no puede estar vacío")
    private String entityType;  // "player", "mob", "npc", "object"

    @Positive(message = "timestamp debe ser positivo")
    private long timestamp;

    @NotNull(message = "position es obligatorio")
    private Position position;

    @NotNull(message = "rotation es obligatorio")
    private Rotation rotation;

    @NotNull(message = "attributes no puede ser null")
    private Map<String, Object> attributes;

    private Action action; // opcional

    // Getters y setters...

    public static class Position {
        @NotNull private Double x;
        @NotNull private Double y;
        @NotNull private Double z;

        @AssertTrue(message = "posición fuera de límites válidos")
        public boolean isValid() {
            return x >= -1000 && x <= 1000
                    && y >= 0 && y <= 500   // suelo = 0, altura máxima = 500
                    && z >= -1000 && z <= 1000;
        }

        public Position(Double x, Double y, Double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Double getX() {
            return x;
        }

        public void setX(Double x) {
            this.x = x;
        }

        public Double getY() {
            return y;
        }

        public void setY(Double y) {
            this.y = y;
        }

        public Double getZ() {
            return z;
        }

        public void setZ(Double z) {
            this.z = z;
        }
    }

    @AssertTrue(message = "hp inválido")
    public boolean isHpValid() {
        if (attributes == null) return true;
        Object hp = attributes.get("hp");
        if (hp instanceof Integer i) {
            return i >= 0 && i <= 100;
        }
        return true; // si no está presente, no valida
    }

    @AssertTrue(message = "mp inválido")
    public boolean isMpValid() {
        if (attributes == null) return true;
        Object mp = attributes.get("mp");
        if (mp instanceof Integer i) {
            return i >= 0;
        }
        return true;
    }
    @AssertTrue(message = "acción inválida")
    public boolean isActionValid() {
        if (action == null) return true;
        if (action.getType() == null || action.getType().isBlank()) return false;
        return action.getDamage() >= 0;
    }

    public boolean isPlayerType() {

        return false;
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

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Rotation getRotation() {
        return rotation;
    }

    public void setRotation(Rotation rotation) {
        this.rotation = rotation;
    }


    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }
}


