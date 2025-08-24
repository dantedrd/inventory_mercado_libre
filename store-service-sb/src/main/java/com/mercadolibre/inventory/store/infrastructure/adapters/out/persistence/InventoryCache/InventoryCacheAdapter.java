package com.mercadolibre.inventory.store.infrastructure.adapters.out.persistence.InventoryCache;

import com.mercadolibre.inventory.store.domain.model.InventoryCache;
import com.mercadolibre.inventory.store.domain.model.InventoryCachePageable;
import com.mercadolibre.inventory.store.domain.ports.InventoryCachePort;
import com.mercadolibre.inventory.shared.models.PageableRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class InventoryCacheAdapter implements InventoryCachePort {
    private final InventoryCacheJpa jpa;

    public InventoryCacheAdapter(InventoryCacheJpa jpa) {
        this.jpa = jpa;
    }

    @Override
    public InventoryCache save(InventoryCache cache) {
        InventoryCacheEntity entity = InventoryCacheTransformer.toEntity(cache);
        InventoryCacheEntity saved = jpa.save(entity);
        return InventoryCacheTransformer.toDomain(saved);
    }

    @Override
    public InventoryCache findBySku(String sku) {
        return jpa.findById(sku)
                .map(InventoryCacheTransformer::toDomain).orElseGet(() -> {
                    InventoryCache x = new InventoryCache();
                    x.setSku(sku);
                    x.setOnHand(0);
                    x.setReserved(0);
                    x.setCommitted(0);
                    x.setAvailable(0);
                    x.setVersion(null);
                    return x;
                });
    }


    @Override
    public InventoryCachePageable listInventoryCaches(PageableRequest pageableRequest) {
        PageRequest pageRequest = PageRequest.of(
                pageableRequest.getPage(),
                pageableRequest.getSize(),
                Sort.by(pageableRequest.getSortBy() != null ? pageableRequest.getSortBy() : "sku").descending()
        );
        var page = jpa.findAll(pageRequest);
        List<InventoryCache> items = page.getContent().stream()
                .map(InventoryCacheTransformer::toDomain)
                .toList();
        return new InventoryCachePageable(
                items,
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}
