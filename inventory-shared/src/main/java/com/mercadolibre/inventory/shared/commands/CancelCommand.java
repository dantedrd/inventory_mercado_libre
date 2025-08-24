package com.mercadolibre.inventory.shared.commands;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CancelCommand {
    @NotBlank
    String sku;

    @NotBlank
    String reservationId;

    String reason;
}
