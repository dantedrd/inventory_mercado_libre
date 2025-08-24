package com.mercadolibre.inventory.store.application.usecase.inventorycache;

import com.mercadolibre.inventory.store.domain.model.InventoryCachePageable;
import com.mercadolibre.inventory.store.domain.ports.InventoryCachePort;
import com.mercadolibre.inventory.shared.models.PageableRequest;

public class ListInventoryCacheUseCase {
    private final InventoryCachePort inventoryCachePort;

    public ListInventoryCacheUseCase(InventoryCachePort inventoryCachePort) {
        this.inventoryCachePort = inventoryCachePort;
    }

    public InventoryCachePageable execute(PageableRequest pageableRequest) {
        return inventoryCachePort.listInventoryCaches(pageableRequest);
    }
}
