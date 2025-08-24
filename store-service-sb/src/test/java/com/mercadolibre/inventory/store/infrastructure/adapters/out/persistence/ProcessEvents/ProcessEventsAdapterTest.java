package com.mercadolibre.inventory.store.infrastructure.adapters.out.persistence.ProcessEvents;

import com.mercadolibre.inventory.store.domain.model.ProcessEvents;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProcessEventsAdapterTest {
    @Mock
    private ProcessEventsJpa jpa;
    @InjectMocks
    private ProcessEventsAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new ProcessEventsAdapter(jpa);
    }

    @Test
    void testSave() {
        ProcessEvents event = new ProcessEvents();
        ProcessEventsEntity entity = new ProcessEventsEntity();
        ProcessEventsEntity savedEntity = new ProcessEventsEntity();
        when(jpa.save(any())).thenReturn(savedEntity);
        try (var transformerMock = Mockito.mockStatic(ProcessEventsTransformer.class)) {
            transformerMock.when(() -> ProcessEventsTransformer.toEntity(event)).thenReturn(entity);
            transformerMock.when(() -> ProcessEventsTransformer.toDomain(savedEntity)).thenReturn(event);
            ProcessEvents result = adapter.save(event);
            assertEquals(event, result);
        }
    }

    @Test
    void testExistEventIdTrue() {
        when(jpa.existsById("event123")).thenReturn(true);
        boolean exists = adapter.existEventId("event123");
        assertTrue(exists);
    }

    @Test
    void testExistEventIdFalse() {
        when(jpa.existsById("event456")).thenReturn(false);
        boolean exists = adapter.existEventId("event456");
        assertFalse(exists);
    }
}

