package com.mmo.service.game_state.application.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;

public class Position {
    @NotNull
    private Double x;
    @NotNull
    private Double y;
    @NotNull
    private Double z;

    @AssertTrue(message = "posición fuera de límites válidos")
    public boolean isValid() {
        return x >= -1000 && x <= 1000
                && y >= 0 && y <= 500   // suelo = 0, altura máxima = 500
                && z >= -1000 && z <= 1000;
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
