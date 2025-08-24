package com.mercadolibre.inventory.store.infrastructure.adapters.out.persistence.InventoryCache;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryCacheJpa extends JpaRepository<InventoryCacheEntity, String> {
    Page<InventoryCacheEntity> findAll(Pageable pageable);
}
