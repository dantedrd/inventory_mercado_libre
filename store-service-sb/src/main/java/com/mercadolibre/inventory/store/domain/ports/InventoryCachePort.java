package com.mercadolibre.inventory.store.domain.ports;

import com.mercadolibre.inventory.store.domain.model.InventoryCache;
import com.mercadolibre.inventory.store.domain.model.InventoryCachePageable;
import com.mercadolibre.inventory.shared.models.PageableRequest;

public interface InventoryCachePort {
    InventoryCache save(InventoryCache cache);
    InventoryCache findBySku(String sku);
    InventoryCachePageable listInventoryCaches(PageableRequest pageableRequest);
}
