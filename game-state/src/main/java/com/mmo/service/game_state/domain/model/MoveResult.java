package com.mmo.service.game_state.domain.model;


public class MoveResult {
    private String entityId;
    private String type;
    private float newX;
    private float newY;
    private float newZ;
    private float newRotation;
    private float newSpeed;
    private float animationBlend;
    private float motionSpeed;
    private float verticalVelocity;
    private float rotationVelocity;
    private float targetRotation;
    private float inputId;

    public MoveResult(String entityId, String type, float newX, float newY, float newZ, float newRotation, float newSpeed, float animationBlend, float motionSpeed, float verticalVelocity, float rotationVelocity, float targetRotation, float inputId) {
        this.entityId = entityId;
        this.type = type;
        this.newX = newX;
        this.newY = newY;
        this.newZ = newZ;
        this.newRotation = newRotation;
        this.newSpeed = newSpeed;
        this.animationBlend = animationBlend;
        this.motionSpeed = motionSpeed;
        this.verticalVelocity = verticalVelocity;
        this.rotationVelocity = rotationVelocity;
        this.targetRotation = targetRotation;
        this.inputId = inputId;
    }

    public float getRotationVelocity() {
        return rotationVelocity;
    }

    public void setRotationVelocity(float rotationVelocity) {
        this.rotationVelocity = rotationVelocity;
    }

    public float getTargetRotation() {
        return targetRotation;
    }

    public void setTargetRotation(float targetRotation) {
        this.targetRotation = targetRotation;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public float getNewX() {
        return newX;
    }

    public void setNewX(float newX) {
        this.newX = newX;
    }

    public float getNewY() {
        return newY;
    }

    public void setNewY(float newY) {
        this.newY = newY;
    }

    public float getNewZ() {
        return newZ;
    }

    public void setNewZ(float newZ) {
        this.newZ = newZ;
    }

    public float getNewRotation() {
        return newRotation;
    }

    public void setNewRotation(float newRotation) {
        this.newRotation = newRotation;
    }

    public float getNewSpeed() {
        return newSpeed;
    }

    public void setNewSpeed(float newSpeed) {
        this.newSpeed = newSpeed;
    }

    public float getAnimationBlend() {
        return animationBlend;
    }

    public void setAnimationBlend(float animationBlend) {
        this.animationBlend = animationBlend;
    }

    public float getMotionSpeed() {
        return motionSpeed;
    }

    public void setMotionSpeed(float motionSpeed) {
        this.motionSpeed = motionSpeed;
    }

    public float getVerticalVelocity() {
        return verticalVelocity;
    }

    public void setVerticalVelocity(float verticalVelocity) {
        this.verticalVelocity = verticalVelocity;
    }
// Getters & setters opcionales


    public float getInputId() {
        return inputId;
    }

    public void setInputId(float inputId) {
        this.inputId = inputId;
    }

    @Override
    public String toString() {
        return "MoveResult{" +
                "entityId='" + entityId + '\'' +
                ", type='" + type + '\'' +
                ", newX=" + newX +
                ", newY=" + newY +
                ", newZ=" + newZ +
                ", newRotation=" + newRotation +
                ", newSpeed=" + newSpeed +
                ", animationBlend=" + animationBlend +
                ", motionSpeed=" + motionSpeed +
                ", verticalVelocity=" + verticalVelocity +
                ", rotationVelocity=" + rotationVelocity +
                ", targetRotation=" + targetRotation +
                ", inputId=" + inputId +
                '}';
    }
}
