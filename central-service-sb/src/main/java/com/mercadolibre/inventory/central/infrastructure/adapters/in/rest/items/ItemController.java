package com.mercadolibre.inventory.central.infrastructure.adapters.in.rest.items;

import com.mercadolibre.inventory.central.application.usecase.RegisterItemUseCase;
import com.mercadolibre.inventory.central.domain.model.Item;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/admin/items")
public class ItemController {
    private final RegisterItemUseCase registerItemUseCase;

    public ItemController(RegisterItemUseCase registerItemUseCase) {
        this.registerItemUseCase = registerItemUseCase;
    }

    @PostMapping
    public ResponseEntity<Item> registerItem(@Valid @RequestBody RegisterItemCommand command) {
        Item item = Item.builder()
                .sku(command.getSku())
                .onHand(command.getOnHand())
                .build();
        Item result = registerItemUseCase.execute(item);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{sku}")
    public ResponseEntity<Item> update(@PathVariable String sku, @Valid @RequestBody RegisterItemCommand req) {
        Item item = Item.builder()
                .sku(sku)
                .onHand(req.getOnHand())
                .build();
        Item saved = registerItemUseCase.execute(item);
        return ResponseEntity.ok(saved);
    }

}
