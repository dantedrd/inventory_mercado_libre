package com.mercadolibre.inventory.central.infrastructure.adapters.out.persistence.Item;

import com.mercadolibre.inventory.central.domain.model.Item;

public class ItemTransformer {
    public static ItemEntity toEntity(Item item) {
        if (item == null) return null;
        return ItemEntity.builder()
                .sku(item.getSku())
                .onHand(item.getOnHand())
                .reserved(item.getReserved())
                .committed(item.getCommitted())
                .version(item.getVersion())
                .updatedAt(item.getUpdatedAt())
                .build();
    }

    public static Item toDomain(ItemEntity entity) {
        if (entity == null) return null;
        return Item.builder()
                .sku(entity.getSku())
                .onHand(entity.getOnHand())
                .reserved(entity.getReserved())
                .committed(entity.getCommitted())
                .version(entity.getVersion())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}

