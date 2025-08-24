package com.mercadolibre.inventory.store.infrastructure.adapters.out.persistence.InventoryCache;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;


@Entity
@Table(name = "inventory_cache")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryCacheEntity {
    @Id
    private String sku;
    private int onHand;
    private int reserved;
    private int committed;
    private int available;
    private Long version;
    private OffsetDateTime updatedAt;

}
