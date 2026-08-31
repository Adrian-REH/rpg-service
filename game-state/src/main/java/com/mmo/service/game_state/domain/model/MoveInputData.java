package com.mmo.service.game_state.domain.model;


public class MoveInputData {
    private String type;
    private String entityId;
    private String zoneId;
    private float moveX;
    private float moveY;
    private boolean sprint;
    private boolean analogMovement;
    private boolean grounded;
    private float currentHorizontalSpeed;
    private float verticalVelocity;
    private float deltaTime;
    private float cameraYaw;
    private float rotationY;
    private float posX;
    private float posY;
    private float posZ;
    private float rotationVelocity;
    private float animationBlend = 0f;

    private float moveSpeed = 5f;
    private float sprintSpeed = 8f;
    private float inputId;


// Getters y Setters

    // ... Genera con tu IDE todos los getters/setters

    // Constructor vacío o parametrizado según necesidad


    public float getRotationVelocity() {
        return rotationVelocity;
    }

    public void setRotationVelocity(float rotationVelocity) {
        this.rotationVelocity = rotationVelocity;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public float getAnimationBlend() {
        return animationBlend;
    }

    public void setAnimationBlend(float animationBlend) {
        this.animationBlend = animationBlend;
    }

    public MoveInputData() {
    }

    public MoveInputData(String type, String entityId, String zoneId, float moveX, float moveY, boolean sprint, boolean analogMovement, boolean grounded, float currentHorizontalSpeed, float verticalVelocity, float deltaTime, float cameraYaw, float rotationY, float posX, float posY, float posZ, float rotationVelocity, float animationBlend, float moveSpeed, float sprintSpeed, float inputId) {
        this.type = type;
        this.entityId = entityId;
        this.zoneId = zoneId;
        this.moveX = moveX;
        this.moveY = moveY;
        this.sprint = sprint;
        this.analogMovement = analogMovement;
        this.grounded = grounded;
        this.currentHorizontalSpeed = currentHorizontalSpeed;
        this.verticalVelocity = verticalVelocity;
        this.deltaTime = deltaTime;
        this.cameraYaw = cameraYaw;
        this.rotationY = rotationY;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.rotationVelocity = rotationVelocity;
        this.animationBlend = animationBlend;
        this.moveSpeed = moveSpeed;
        this.sprintSpeed = sprintSpeed;
        this.inputId = inputId;
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

    public float getMoveX() {
        return moveX;
    }

    public void setMoveX(float moveX) {
        this.moveX = moveX;
    }

    public float getMoveY() {
        return moveY;
    }

    public void setMoveY(float moveY) {
        this.moveY = moveY;
    }

    public boolean isSprint() {
        return sprint;
    }

    public void setSprint(boolean sprint) {
        this.sprint = sprint;
    }

    public boolean isAnalogMovement() {
        return analogMovement;
    }

    public void setAnalogMovement(boolean analogMovement) {
        this.analogMovement = analogMovement;
    }

    public boolean isGrounded() {
        return grounded;
    }

    public void setGrounded(boolean grounded) {
        this.grounded = grounded;
    }

    public float getCurrentHorizontalSpeed() {
        return currentHorizontalSpeed;
    }

    public void setCurrentHorizontalSpeed(float currentHorizontalSpeed) {
        this.currentHorizontalSpeed = currentHorizontalSpeed;
    }

    public float getVerticalVelocity() {
        return verticalVelocity;
    }

    public void setVerticalVelocity(float verticalVelocity) {
        this.verticalVelocity = verticalVelocity;
    }

    public float getDeltaTime() {
        return deltaTime;
    }

    public void setDeltaTime(float deltaTime) {
        this.deltaTime = deltaTime;
    }

    public float getCameraYaw() {
        return cameraYaw;
    }

    public void setCameraYaw(float cameraYaw) {
        this.cameraYaw = cameraYaw;
    }

    public float getRotationY() {
        return rotationY;
    }

    public void setRotationY(float rotationY) {
        this.rotationY = rotationY;
    }

    public float getPosX() {
        return posX;
    }

    public void setPosX(float posX) {
        this.posX = posX;
    }

    public float getPosY() {
        return posY;
    }

    public void setPosY(float posY) {
        this.posY = posY;
    }

    public float getPosZ() {
        return posZ;
    }

    public void setPosZ(float posZ) {
        this.posZ = posZ;
    }

    public float getMoveSpeed() {
        return moveSpeed;
    }

    public void setMoveSpeed(float moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    public float getSprintSpeed() {
        return sprintSpeed;
    }

    public void setSprintSpeed(float sprintSpeed) {
        this.sprintSpeed = sprintSpeed;
    }

    public float getInputId() {
        return inputId;
    }

    public void setInputId(float inputId) {
        this.inputId = inputId;
    }

    @Override
    public String toString() {
        return "MoveInputData{" +
                "type='" + type + '\'' +
                ", entityId='" + entityId + '\'' +
                ", zoneId='" + zoneId + '\'' +
                ", moveX=" + moveX +
                ", moveY=" + moveY +
                ", sprint=" + sprint +
                ", analogMovement=" + analogMovement +
                ", grounded=" + grounded +
                ", currentHorizontalSpeed=" + currentHorizontalSpeed +
                ", verticalVelocity=" + verticalVelocity +
                ", deltaTime=" + deltaTime +
                ", cameraYaw=" + cameraYaw +
                ", rotationY=" + rotationY +
                ", posX=" + posX +
                ", posY=" + posY +
                ", posZ=" + posZ +
                ", rotationVelocity=" + rotationVelocity +
                ", animationBlend=" + animationBlend +
                ", moveSpeed=" + moveSpeed +
                ", sprintSpeed=" + sprintSpeed +
                ", inputId=" + inputId +
                '}';
    }

}
