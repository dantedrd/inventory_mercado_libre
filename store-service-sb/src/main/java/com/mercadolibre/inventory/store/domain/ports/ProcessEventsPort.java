package com.mercadolibre.inventory.store.domain.ports;

import com.mercadolibre.inventory.store.domain.model.ProcessEvents;

public interface ProcessEventsPort {
    ProcessEvents save(ProcessEvents event);
    boolean existEventId(String eventId);
}

