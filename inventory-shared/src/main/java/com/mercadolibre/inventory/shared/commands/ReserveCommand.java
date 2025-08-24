package com.mercadolibre.inventory.shared.commands;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReserveCommand {
    @NotBlank
    String sku;

    @NotBlank
    String siteId;

    @NotBlank
    String reservationId;

    @Positive
    @Min(1)
    int qty;
}
