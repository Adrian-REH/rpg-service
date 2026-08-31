package com.mmo.service.game_state.application.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

public class ItemStateDTO {
    @NotBlank(message = "entityId no puede estar vacío")
    String playerId;
    @NotBlank(message = "itemId no puede estar vacío")
    String itemId;
    Double playerX;
    Double playerZ;
    Action action;


    public ItemStateDTO(String playerId, String itemId, Double playerX, Double playerZ) {
        this.playerId = playerId;
        this.itemId = itemId;
        this.playerX = playerX;
        this.playerZ = playerZ;
    }



    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }
    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public Double getPlayerX() {
        return playerX;
    }

    public void setPlayerX(Double playerX) {
        this.playerX = playerX;
    }

    public Double getPlayerZ() {
        return playerZ;
    }

    public void setPlayerZ(Double playerZ) {
        this.playerZ = playerZ;
    }

    public static class Action {
        @NotBlank(message = "type de acción es obligatorio")
        private String type;

        public Action(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public boolean isPickUp() {
            return Objects.equals(type, "pickup_item");
        }
    }
}
