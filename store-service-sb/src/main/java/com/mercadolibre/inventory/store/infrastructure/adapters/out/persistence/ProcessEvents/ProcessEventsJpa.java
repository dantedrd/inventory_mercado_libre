package com.mercadolibre.inventory.store.infrastructure.adapters.out.persistence.ProcessEvents;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProcessEventsJpa extends JpaRepository<ProcessEventsEntity, String> {

}

