package com.mmo.service.game_state.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class Action {
    @NotBlank(message = "type de acción es obligatorio")
    private String type;
    private String targetId; // opcional
    @Min(value = 0, message = "damage no puede ser negativo")
    private int damage;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
}
