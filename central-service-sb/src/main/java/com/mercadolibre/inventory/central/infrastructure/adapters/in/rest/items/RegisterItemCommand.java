package com.mercadolibre.inventory.central.infrastructure.adapters.in.rest.items;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterItemCommand {

    @NotBlank
    private String sku;

    @Positive
    @Min(0)
    private int onHand;
}

