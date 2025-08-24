package com.mercadolibre.inventory.store.application.usecase.inventorycache;

import com.mercadolibre.inventory.store.domain.model.InventoryCache;
import com.mercadolibre.inventory.store.domain.ports.InventoryCachePort;
import java.util.Optional;

public class GetInventoryCacheUseCase {
    private final InventoryCachePort inventoryCachePort;

    public GetInventoryCacheUseCase(InventoryCachePort inventoryCachePort) {
        this.inventoryCachePort = inventoryCachePort;
    }

    public Optional<InventoryCache> execute(String sku) {
        InventoryCache cache = inventoryCachePort.findBySku(sku);
        return Optional.ofNullable(cache);
    }
}

