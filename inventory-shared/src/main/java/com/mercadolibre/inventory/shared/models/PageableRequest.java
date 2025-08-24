package com.mercadolibre.inventory.shared.models;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageableRequest {
    int page;
    int size;
    String sortBy;
}
