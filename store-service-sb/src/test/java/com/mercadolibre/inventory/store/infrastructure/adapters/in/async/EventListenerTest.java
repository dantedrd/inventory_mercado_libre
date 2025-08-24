package com.mercadolibre.inventory.store.infrastructure.adapters.in.async;

import com.mercadolibre.inventory.store.application.usecase.CentralNotification.CentralNotificationSaveUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventListenerTest {
    @Mock
    private CentralNotificationSaveUseCase centralNotificationSaveUseCase;
    private EventListener eventListener;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        eventListener = new EventListener(centralNotificationSaveUseCase);
    }

    @Test
    void testOnEventCallsUseCaseWithCorrectParams() throws Exception {
        String json = "{ 'key': 'value' }".replace('"', '\'');
        byte[] body = json.getBytes();
        String routingKey = "test.routing";

        ArgumentCaptor<Map> eventCaptor = ArgumentCaptor.forClass(Map.class);
        ArgumentCaptor<String> routingCaptor = ArgumentCaptor.forClass(String.class);

        eventListener.onEvent(body, routingKey);

        verify(centralNotificationSaveUseCase, times(1)).executeUpdateInventory(eventCaptor.capture(), routingCaptor.capture());
        Map<?,?> event = eventCaptor.getValue();
        assertEquals("value", event.get("key"));
        assertEquals(routingKey, routingCaptor.getValue());
    }

    @Test
    void testOnEventWithInvalidJsonThrowsException() {
        byte[] body = "invalid json".getBytes();
        String routingKey = "test.routing";
        assertThrows(Exception.class, () -> eventListener.onEvent(body, routingKey));
    }
}

