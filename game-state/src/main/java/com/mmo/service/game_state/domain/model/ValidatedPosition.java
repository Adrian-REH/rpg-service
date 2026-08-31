package com.mmo.service.game_state.domain.model;

import com.mmo.service.game_state.application.dto.EntityStateDTO;

/**
 * Resultado de la validación de movimiento
 */

public class ValidatedPosition {

    /**
     * Si el movimiento fue aceptado
     */
    private boolean accepted;

    /**
     * Posición validada (puede ser diferente a la solicitada si hubo corrección)
     */
    private Position position;

    /**
     * Si hubo una corrección de posición por colisión o anti-cheat
     */
    private boolean corrected;

    /**
     * Razón del rechazo o corrección
     */
    private String reason;

    /**
     * Timestamp de la validación
     */
    private long timestamp;

    /**
     * Información adicional sobre la validación
     */
    private ValidationMetadata metadata;

    // ==================== Factory Methods ====================

    /**
     * Movimiento aceptado sin correcciones
     */
    public static ValidatedPosition accepted(Position position) {
        ValidatedPosition result = new ValidatedPosition();
        result.setAccepted(true);
        result.setPosition(position);
        result.setCorrected(false);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    /**
     * Movimiento aceptado pero con corrección de posición
     */
    public static ValidatedPosition corrected(Position correctedPosition, String reason) {
        ValidatedPosition result = new ValidatedPosition();
        result.setAccepted(true);
        result.setPosition(correctedPosition);
        result.setCorrected(true);
        result.setReason(reason);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    /**
     * Movimiento rechazado completamente
     */
    public static ValidatedPosition rejected(Position currentPosition, String reason) {
        ValidatedPosition result = new ValidatedPosition();
        result.setAccepted(false);
        result.setPosition(currentPosition);
        result.setCorrected(false);
        result.setReason(reason);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    /**
     * Error en la validación
     */
    public static ValidatedPosition error(String errorMessage) {
        ValidatedPosition result = new ValidatedPosition();
        result.setAccepted(false);
        result.setReason(errorMessage);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    // ==================== Helper Methods ====================

    /**
     * Si la posición fue modificada respecto a la solicitada
     */
    public boolean wasModified() {
        return corrected;
    }

    /**
     * Si necesita notificar al cliente sobre el cambio
     */
    public boolean requiresClientUpdate() {
        return !accepted || corrected;
    }

    /**
     * Agregar metadata adicional
     */
    public ValidatedPosition withMetadata(ValidationMetadata metadata) {
        this.metadata = metadata;
        return this;
    }

    /**
     * Metadata adicional de la validación
     */

    public static class ValidationMetadata {
        private Double distanceMoved;
        private Double maxAllowedDistance;
        private Boolean hadCollision;
        private String collisionType; // "terrain", "entity", "boundary"
        private Integer nearbyEntities;
        private String validatorVersion;


        public static ValidationMetadata create() {
            return new ValidationMetadata();
        }

        public ValidationMetadata distanceMoved(Double distance) {
            this.distanceMoved = distance;
            return this;
        }

        public ValidationMetadata maxAllowedDistance(Double max) {
            this.maxAllowedDistance = max;
            return this;
        }

        public ValidationMetadata hadCollision(Boolean collision) {
            this.hadCollision = collision;
            return this;
        }

        public ValidationMetadata collisionType(String type) {
            this.collisionType = type;
            return this;
        }

        public ValidationMetadata nearbyEntities(Integer count) {
            this.nearbyEntities = count;
            return this;
        }

        public Double getDistanceMoved() {
            return distanceMoved;
        }

        public void setDistanceMoved(Double distanceMoved) {
            this.distanceMoved = distanceMoved;
        }

        public Double getMaxAllowedDistance() {
            return maxAllowedDistance;
        }

        public void setMaxAllowedDistance(Double maxAllowedDistance) {
            this.maxAllowedDistance = maxAllowedDistance;
        }

        public Boolean getHadCollision() {
            return hadCollision;
        }

        public void setHadCollision(Boolean hadCollision) {
            this.hadCollision = hadCollision;
        }

        public String getCollisionType() {
            return collisionType;
        }

        public void setCollisionType(String collisionType) {
            this.collisionType = collisionType;
        }

        public Integer getNearbyEntities() {
            return nearbyEntities;
        }

        public void setNearbyEntities(Integer nearbyEntities) {
            this.nearbyEntities = nearbyEntities;
        }

        public String getValidatorVersion() {
            return validatorVersion;
        }

        public void setValidatorVersion(String validatorVersion) {
            this.validatorVersion = validatorVersion;
        }
    }

    // ==================== Position Inner Class ====================

    /**
     * Posición 3D en el mundo
     */
    public static class Position {
        private Double x;
        private Double y;
        private Double z;

        public Position(Double x, Double y, Double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        /**
         * Calcular distancia a otra posición
         */
        public double distanceTo(Position other) {
            return Math.sqrt(
                    Math.pow(other.x - this.x, 2) +
                            Math.pow(other.y - this.y, 2) +
                            Math.pow(other.z - this.z, 2)
            );
        }

        /**
         * Distancia 2D (ignorando Y)
         */
        public double distance2DTo(Position other) {
            return Math.sqrt(
                    Math.pow(other.x - this.x, 2) +
                            Math.pow(other.z - this.z, 2)
            );
        }

        /**
         * Clonar posición
         */
        public Position copy() {
            return new Position(this.x, this.y, this.z);
        }

        /**
         * Interpolar hacia otra posición
         */
        public Position lerp(Position target, double alpha) {
            return new Position(
                    this.x + (target.x - this.x) * alpha,
                    this.y + (target.y - this.y) * alpha,
                    this.z + (target.z - this.z) * alpha
            );
        }

        /**
         * Verificar si está dentro de límites
         */
        public boolean isWithinBounds(double minX, double maxX, double minZ, double maxZ) {
            return x >= minX && x <= maxX && z >= minZ && z <= maxZ;
        }

        /**
         * Crear desde DTO
         */
        public static Position fromDTO(EntityStateDTO.Position dto) {
            return new Position(dto.getX(), dto.getY(), dto.getZ());
        }



        @Override
        public String toString() {
            return String.format("(%.2f, %.2f, %.2f)", x, y, z);
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

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public boolean isCorrected() {
        return corrected;
    }

    public void setCorrected(boolean corrected) {
        this.corrected = corrected;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public ValidationMetadata getMetadata() {
        return metadata;
    }

    public void setMetadata(ValidationMetadata metadata) {
        this.metadata = metadata;
    }
}