package com.mmo.service.game_state.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mmo.service.game_state.infrastructure.persistence.entity.PlayerInventoryEntity;
import com.mmo.service.game_state.infrastructure.persistence.repository.PlayerInventoryRepository;
import com.mmo.service.game_state.application.dto.ItemStateDTO;
import com.mmo.service.game_state.infrastructure.messager.ItemInventoryProducer;
import com.mmo.service.game_state.infrastructure.persistence.entity.ItemTemplateEntity;
import com.mmo.service.game_state.infrastructure.persistence.entity.WorldItemEntity;
import com.mmo.service.game_state.infrastructure.persistence.repository.ItemTemplateRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
public class ItemInteractionService {

    private final GameWorldService worldService;
    private final PlayerInventoryRepository inventoryRepository;
    private final ItemTemplateRepository templateRepository;
    private final ItemInventoryProducer itemInventoryProducer;
    private final ObjectMapper objectMapper;

    public ItemInteractionService(GameWorldService worldService, PlayerInventoryRepository inventoryRepository, ItemTemplateRepository templateRepository, ItemInventoryProducer itemInventoryProducer, ObjectMapper objectMapper) {
        this.worldService = worldService;
        this.inventoryRepository = inventoryRepository;
        this.templateRepository = templateRepository;
        this.itemInventoryProducer = itemInventoryProducer;
        this.objectMapper = objectMapper;
    }

    /**
     *
     * Recoger item del mundo
     */
    public Mono<ItemPickupResult> pickupItem(ItemStateDTO dto) {
        return worldService.pickupItem(dto.getPlayerId(), dto.getItemId(), dto.getPlayerX(), dto.getPlayerZ())
                .flatMap(worldItem ->
                        // Obtener template del item
                        templateRepository.findById(worldItem.getItemTemplateId())
                                .flatMap(template ->
                                        // Agregar al inventario
                                        addToInventory(dto.getPlayerId(), template, worldItem.getQuantity())
                                                .map(inventory -> {
                                                    // Notificar al cliente
                                                    notifyItemPickup(dto.getPlayerId(), worldItem, template).subscribe();

                                                    return new ItemPickupResult(true,
                                                            template.getItemName(),
                                                            worldItem.getQuantity());
                                                })
                                )
                )
                .onErrorResume(error -> {
                    System.out.println("Error en pickup: " + error.getMessage());
                    return Mono.just(new ItemPickupResult(false, null, 0, error.getMessage()));
                });
    }

    /**
     * Agregar item al inventario del jugador
     */
    private Mono<PlayerInventoryEntity> addToInventory(String playerId,
                                                       ItemTemplateEntity template,
                                                       Integer quantity) {
        // Buscar si ya tiene el item (para stackear)
        return inventoryRepository.findByPlayerIdAndItemTemplateId(playerId, template.getItemTemplateId())
                .flatMap(existing -> {
                    // Si existe y es stackeable, aumentar cantidad
                    if (template.getStackSize() > 1) {
                        existing.setQuantity(existing.getQuantity() + quantity);
                        return inventoryRepository.save(existing);
                    }

                    // Si no es stackeable, crear nueva entrada
                    return createNewInventorySlot(playerId, template, quantity);
                })
                .switchIfEmpty(createNewInventorySlot(playerId, template, quantity));
    }

    private Mono<PlayerInventoryEntity> createNewInventorySlot(String playerId,
                                                               ItemTemplateEntity template,
                                                               Integer quantity) {
        return inventoryRepository.findFirstAvailableSlot(playerId)
                .flatMap(slot -> {
                    PlayerInventoryEntity inventory = new PlayerInventoryEntity();
                    inventory.setInventoryId("inv-" + UUID.randomUUID());
                    inventory.setPlayerId(playerId);
                    inventory.setItemTemplateId(template.getItemTemplateId());
                    inventory.setSlotNumber(slot);
                    inventory.setQuantity(quantity);
                    inventory.setIsEquipped(false);
                    inventory.setAcquiredAt(LocalDateTime.now());

                    return inventoryRepository.save(inventory);
                });
    }

    private Mono<Void> notifyItemPickup(String playerId, WorldItemEntity worldItem,
                                        ItemTemplateEntity template) {
        return Mono.fromRunnable(() -> {
            try {
                Map<String, Object> notification = Map.of(
                        "entityId", playerId,
                        "type", "item_picked_up",
                        "itemId", worldItem.getWorldItemId(),
                        "itemName", template.getItemName(),
                        "itemType", template.getItemType(),
                        "quantity", worldItem.getQuantity(),
                        "iconUrl", template.getIconUrl()
                );

                itemInventoryProducer.sendUpdate(objectMapper.writeValueAsString(notification));

            } catch (Exception e) {
                System.out.println("Error notificando pickup: " + e);
            }
        });
    }

    public static class ItemPickupResult {
        private boolean success;
        private String itemName;
        private Integer quantity;
        private String errorMessage;

        public ItemPickupResult(boolean success, String itemName, Integer quantity) {
            this(success, itemName, quantity, null);
        }

        public ItemPickupResult(boolean success, String itemName, Integer quantity, String errorMessage) {
            this.success = success;
            this.itemName = itemName;
            this.quantity = quantity;
            this.errorMessage = errorMessage;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }
}