package com.mercadolibre.inventory.store.infrastructure.adapters.out.persistence.ProcessEvents;

import com.mercadolibre.inventory.store.domain.model.ProcessEvents;
import com.mercadolibre.inventory.store.domain.ports.ProcessEventsPort;
import org.springframework.stereotype.Component;

@Component
public class ProcessEventsAdapter implements ProcessEventsPort {
    private final ProcessEventsJpa jpa;

    public ProcessEventsAdapter(ProcessEventsJpa jpa) {
        this.jpa = jpa;
    }

    @Override
    public ProcessEvents save(ProcessEvents event) {
        ProcessEventsEntity entity = ProcessEventsTransformer.toEntity(event);
        ProcessEventsEntity saved = jpa.save(entity);
        return ProcessEventsTransformer.toDomain(saved);
    }

    @Override
    public boolean existEventId(String eventId) {
        return jpa.existsById(eventId);
    }
}

