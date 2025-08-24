package com.mercadolibre.inventory.store.domain.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Commit {
    private String sku;
    private String reservationId;
}
