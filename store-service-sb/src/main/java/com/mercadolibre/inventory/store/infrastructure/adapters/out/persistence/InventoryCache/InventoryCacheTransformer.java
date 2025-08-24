package com.mercadolibre.inventory.store.infrastructure.adapters.out.persistence.InventoryCache;

import com.mercadolibre.inventory.store.domain.model.InventoryCache;

public class InventoryCacheTransformer {
    public static InventoryCache toDomain(InventoryCacheEntity entity) {
        if (entity == null) return null;
        return InventoryCache.builder()
                .sku(entity.getSku())
                .onHand(entity.getOnHand())
                .reserved(entity.getReserved())
                .committed(entity.getCommitted())
                .available(entity.getAvailable())
                .version(entity.getVersion())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static InventoryCacheEntity toEntity(InventoryCache cache) {
        if (cache == null) return null;
        return InventoryCacheEntity.builder()
                .sku(cache.getSku())
                .onHand(cache.getOnHand())
                .reserved(cache.getReserved())
                .committed(cache.getCommitted())
                .available(cache.getAvailable())
                .version(cache.getVersion())
                .updatedAt(cache.getUpdatedAt())
                .build();
    }
}

