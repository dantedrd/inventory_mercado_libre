package com.mercadolibre.inventory.store.domain.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cancel {
    private String sku;
    private String reservationId;
    private String reason;
}
