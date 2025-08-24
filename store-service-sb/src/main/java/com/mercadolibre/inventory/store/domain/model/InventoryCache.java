package com.mercadolibre.inventory.store.domain.model;

import lombok.*;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryCache {
    private String sku;
    private int onHand;
    private int reserved;
    private int committed;
    private int available;
    private Long version;
    private OffsetDateTime updatedAt;
}

