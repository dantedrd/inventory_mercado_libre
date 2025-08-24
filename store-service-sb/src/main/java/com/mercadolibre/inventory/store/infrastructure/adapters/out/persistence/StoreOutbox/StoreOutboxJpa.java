package com.mercadolibre.inventory.store.infrastructure.adapters.out.persistence.StoreOutbox;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreOutboxJpa extends JpaRepository<StoreOutboxEntity, String> {
    List<StoreOutboxEntity> findTop50ByStatusOrderByCreatedAtAsc(String status);
}
