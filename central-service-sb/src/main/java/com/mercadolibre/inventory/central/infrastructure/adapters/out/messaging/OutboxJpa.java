package com.mercadolibre.inventory.central.infrastructure.adapters.out.messaging;

import com.mercadolibre.inventory.central.infrastructure.adapters.out.persistence.OutboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OutboxJpa extends JpaRepository<OutboxEntity, String> {
  List<OutboxEntity> findTop50ByStatusOrderByCreatedAtAsc(String status);
}
