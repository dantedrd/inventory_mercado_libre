package com.mercadolibre.inventory.store.infrastructure.adapters.out.persistence.ProcessEvents;

import com.mercadolibre.inventory.store.domain.model.ProcessEvents;

public class ProcessEventsTransformer {
    public static ProcessEvents toDomain(ProcessEventsEntity entity) {
        if (entity == null) return null;
        return ProcessEvents.builder()
                .eventId(entity.getEventId())
                .build();
    }

    public static ProcessEventsEntity toEntity(ProcessEvents event) {
        if (event == null) return null;
        return ProcessEventsEntity.builder()
                .eventId(event.getEventId())
                .build();
    }
}

