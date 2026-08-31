package com.mmo.service.game_state.application;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.mmo.service.game_state.infrastructure.persistence.entity.ActiveMobEntity;
import com.mmo.service.game_state.infrastructure.persistence.entity.NPCEntity;
import com.mmo.service.game_state.infrastructure.persistence.entity.WorldItemEntity;
import com.mmo.service.game_state.infrastructure.persistence.entity.ZoneEntity;
import com.mmo.service.game_state.infrastructure.persistence.repository.ActiveMobRepository;
import com.mmo.service.game_state.infrastructure.persistence.repository.NPCRepository;
import com.mmo.service.game_state.infrastructure.persistence.repository.WorldItemRepository;
import com.mmo.service.game_state.infrastructure.persistence.repository.ZoneRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class GameWorldService {

    private final WorldItemRepository itemRepository;
    private final NPCRepository npcRepository;
    private final ActiveMobRepository mobRepository;
    private final ZoneRepository zoneRepository;

    // Cache en memoria con Caffeine
    private final LoadingCache<String, List<WorldItemEntity>> itemCache;
    private final LoadingCache<String, List<NPCEntity>> npcCache;

    @Value("${game.world.cell-size:10.0}")
    private Double cellSize;

    @Value("${game.world.item-pickup-range:2.0}")
    private Double itemPickupRange;

    public GameWorldService(
            WorldItemRepository itemRepository,
            NPCRepository npcRepository,
            ActiveMobRepository mobRepository,
            ZoneRepository zoneRepository) {

        this.itemRepository = itemRepository;
        this.npcRepository = npcRepository;
        this.mobRepository = mobRepository;
        this.zoneRepository = zoneRepository;

        // Configurar cache con 5 minutos TTL
        this.itemCache = Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofMinutes(5))
                .maximumSize(1000)
                .build(key -> new ArrayList<>());

        this.npcCache = Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofMinutes(30))
                .maximumSize(500)
                .build(key -> new ArrayList<>());
    }

    /**
     * Carga inicial de una zona
     */
    public Mono<ZoneData> loadZone(String zoneId) {
        System.out.println("🗺️ Cargando zona: " + zoneId);

        return Mono.zip(
                zoneRepository.findById(zoneId),
                itemRepository.findByZoneIdAndIsAvailable(zoneId, true).collectList(),
                npcRepository.findByZoneIdAndIsActive(zoneId, true).collectList(),
                mobRepository.findByZoneId(zoneId).collectList()
        ).map(tuple -> {
            ZoneData data = new ZoneData();
            data.setZone(tuple.getT1());
            data.setItems(tuple.getT2());
            data.setNpcs(tuple.getT3());
            data.setMobs(tuple.getT4());

            // Cachear
            itemCache.put(zoneId, tuple.getT2());
            npcCache.put(zoneId, tuple.getT3());

            System.out.println("✅ Zona cargada: " +
                    tuple.getT2().size() + " items, " + tuple.getT3().size() + " NPCs, "+tuple.getT4().size() + " mobs");

            return data;
        });
    }

    /**
     * Buscar items cerca de una posición
     */
    public Flux<WorldItemEntity> getItemsNearPosition(String zoneId, Double x, Double z, Double radius) {
        return itemRepository.findByZoneAndRadius(zoneId, x, z, radius)
                .filter(WorldItemEntity::getIsAvailable)
                .doOnNext(item -> System.out.println("📦 Item encontrado: " +
                        item.getWorldItemId() +" en " + item.getPosX() + ", "+ item.getPosZ()));
    }

    /**
     * Buscar NPCs cerca de una posición
     */
    public Flux<NPCEntity> getNPCsNearPosition(String zoneId, Double x, Double z, Double radius) {
        return npcRepository.findByZoneAndRadius(zoneId, x, z, radius)
                .doOnNext(npc -> System.out.println("👤 NPC encontrado: " +
                        npc.getNpcId() + " en " + npc.getPosX() + ", "+ npc.getPosZ()));
    }

    /**
     * Buscar mobs cerca de una posición
     */
    public Flux<ActiveMobEntity> getMobsNearPosition(String zoneId, Double x, Double z, Double radius) {
        return mobRepository.findByZoneAndRadius(zoneId, x, z, radius)
                .doOnNext(mob -> System.out.println("👹 Mob encontrado: " +
                        mob.getMobId() + " en " + mob.getPosX() +"," + mob.getPosZ()));
    }

    /**
     * Verificar colisiones con el terreno
     */
    public Mono<Boolean> hasTerrainCollision(String zoneId, Double x, Double y, Double z) {
        // TODO: Implementar consulta a world_collisions
        return Mono.just(false);
    }

    /**
     * Pickup de item
     */
    public Mono<WorldItemEntity> pickupItem(String playerId, String itemId, Double playerX, Double playerZ) {
        return itemRepository.findById(itemId)
                .flatMap(item -> {
                    // Verificar distancia
                    double distance = Math.sqrt(
                            Math.pow(item.getPosX() - playerX, 2) +
                                    Math.pow(item.getPosZ() - playerZ, 2)
                    );

                    if (distance > itemPickupRange) {
                        return Mono.error(new IllegalStateException(
                                "Item demasiado lejos: " + distance));
                    }

                    if (!item.getIsAvailable()) {
                        return Mono.error(new IllegalStateException(
                                "Item ya no está disponible"));
                    }

                    // Marcar como recogido
                    return itemRepository.markAsPickedUp(itemId)
                            .thenReturn(item);
                })
                .doOnSuccess(item -> {
                    System.out.println("✅ Player " +  playerId +" recogió item {}" + itemId);
                    invalidateItemCache(item.getZoneId());
                })
                .doOnError(error -> System.out.println("❌ Error en pickup: {}" + error.getMessage()));
    }


    /**
     * Spawn de item en el mundo
     */
    public Mono<WorldItemEntity> spawnItem(String itemTemplateId, String zoneId,
                                           Double x, Double y, Double z,
                                           Integer quantity, String droppedBy) {
        WorldItemEntity item = new WorldItemEntity();
        item.setWorldItemId("item-" + UUID.randomUUID());
        item.setItemTemplateId(itemTemplateId);
        item.setZoneId(zoneId);
        item.setPosX(x);
        item.setPosY(y);
        item.setPosZ(z);
        item.setQuantity(quantity);
        item.setIsAvailable(true);
        item.setDroppedBy(droppedBy);
        item.setSpawnedAt(LocalDateTime.now());
        item.setDespawnAt(LocalDateTime.now().plusMinutes(5));

        return itemRepository.save(item)
                .doOnSuccess(saved -> {
                    System.out.println("📦 Item spawneado: " + saved.getWorldItemId() + "en (" + x +", " + y + ", "+ z + ")");
                    invalidateItemCache(zoneId);
                });
    }

    private void invalidateItemCache(String zoneId) {
        itemCache.invalidate(zoneId);
    }

    public Mono<ZoneEntity> getZone(String zoneId) {
        return Mono.empty();
    }

    public static class ZoneData {
        private ZoneEntity zone;
        private List<WorldItemEntity> items;
        private List<NPCEntity> npcs;
        private List<ActiveMobEntity> mobs;

        public ZoneEntity getZone() {
            return zone;
        }

        public void setZone(ZoneEntity zone) {
            this.zone = zone;
        }

        public List<WorldItemEntity> getItems() {
            return items;
        }

        public void setItems(List<WorldItemEntity> items) {
            this.items = items;
        }

        public List<NPCEntity> getNpcs() {
            return npcs;
        }

        public void setNpcs(List<NPCEntity> npcs) {
            this.npcs = npcs;
        }

        public List<ActiveMobEntity> getMobs() {
            return mobs;
        }

        public void setMobs(List<ActiveMobEntity> mobs) {
            this.mobs = mobs;
        }
    }
}