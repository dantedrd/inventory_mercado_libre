package com.mercadolibre.inventory.store.application.usecase.CentralNotification;

import com.mercadolibre.inventory.store.domain.model.InventoryCache;
import com.mercadolibre.inventory.store.domain.model.ProcessEvents;
import com.mercadolibre.inventory.store.domain.ports.InventoryCachePort;
import com.mercadolibre.inventory.store.domain.ports.ProcessEventsPort;
import com.mercadolibre.inventory.shared.Topics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CentralNotificationSaveUseCaseTest {
    @Mock
    private InventoryCachePort inventoryCachePort;
    @Mock
    private ProcessEventsPort processEventsPort;
    private CentralNotificationSaveUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new CentralNotificationSaveUseCase(inventoryCachePort, processEventsPort);
    }

    @Test
    void testEventIdAlreadyExists() {
        Map<String, Object> m = new HashMap<>();
        m.put("eventId", "evt1");
        when(processEventsPort.existEventId("evt1")).thenReturn(true);
        useCase.executeUpdateInventory(m, Topics.RK_RESERVED);
        verify(inventoryCachePort, never()).save(any());
        verify(processEventsPort, never()).save(any());
    }

    @Test
    void testVersionLowerOrEqualInventory() {
        Map<String, Object> m = new HashMap<>();
        m.put("eventId", "evt2");
        m.put("sku", "sku1");
        m.put("qty", 5);
        m.put("version", 2L);
        InventoryCache cache = new InventoryCache();
        cache.setVersion(3L);
        when(processEventsPort.existEventId("evt2")).thenReturn(false);
        when(inventoryCachePort.findBySku("sku1")).thenReturn(cache);
        useCase.executeUpdateInventory(m, Topics.RK_RESERVED);
        verify(inventoryCachePort, never()).save(any());
        verify(processEventsPort, times(1)).save(any(ProcessEvents.class));
    }

    @Test
    void testReservedEvent() {
        Map<String, Object> m = new HashMap<>();
        m.put("eventId", "evt3");
        m.put("sku", "sku2");
        m.put("qty", 10);
        m.put("version", 5L);
        InventoryCache cache = new InventoryCache();
        cache.setReserved(2);
        cache.setOnHand(20);
        cache.setCommitted(3);
        cache.setVersion(4L);
        when(processEventsPort.existEventId("evt3")).thenReturn(false);
        when(inventoryCachePort.findBySku("sku2")).thenReturn(cache);
        useCase.executeUpdateInventory(m, Topics.RK_RESERVED);
        ArgumentCaptor<InventoryCache> captor = ArgumentCaptor.forClass(InventoryCache.class);
        verify(inventoryCachePort, times(1)).save(captor.capture());
        InventoryCache updated = captor.getValue();
        assertEquals(12, updated.getReserved());
        assertEquals(5L, updated.getVersion());
        assertEquals(20 - 12 - 3, updated.getAvailable());
        assertNotNull(updated.getUpdatedAt());
        verify(processEventsPort, times(1)).save(any(ProcessEvents.class));
    }

    @Test
    void testCommittedEvent() {
        Map<String, Object> m = new HashMap<>();
        m.put("eventId", "evt4");
        m.put("sku", "sku3");
        m.put("qty", 7);
        m.put("version", 6L);
        InventoryCache cache = new InventoryCache();
        cache.setReserved(10);
        cache.setCommitted(5);
        cache.setOnHand(30);
        cache.setVersion(5L);
        when(processEventsPort.existEventId("evt4")).thenReturn(false);
        when(inventoryCachePort.findBySku("sku3")).thenReturn(cache);
        useCase.executeUpdateInventory(m, Topics.RK_COMMITTED);
        ArgumentCaptor<InventoryCache> captor = ArgumentCaptor.forClass(InventoryCache.class);
        verify(inventoryCachePort, times(1)).save(captor.capture());
        InventoryCache updated = captor.getValue();
        assertEquals(3, updated.getReserved());
        assertEquals(12, updated.getCommitted());
        assertEquals(30 - 3 - 12, updated.getAvailable());
        assertEquals(6L, updated.getVersion());
        verify(processEventsPort, times(1)).save(any(ProcessEvents.class));
    }

    @Test
    void testCancelEvent() {
        Map<String, Object> m = new HashMap<>();
        m.put("eventId", "evt5");
        m.put("sku", "sku4");
        m.put("qty", 4);
        m.put("version", 7L);
        InventoryCache cache = new InventoryCache();
        cache.setReserved(8);
        cache.setCommitted(2);
        cache.setOnHand(15);
        cache.setVersion(6L);
        when(processEventsPort.existEventId("evt5")).thenReturn(false);
        when(inventoryCachePort.findBySku("sku4")).thenReturn(cache);
        useCase.executeUpdateInventory(m, Topics.RK_CMD_CANCEL);
        ArgumentCaptor<InventoryCache> captor = ArgumentCaptor.forClass(InventoryCache.class);
        verify(inventoryCachePort, times(1)).save(captor.capture());
        InventoryCache updated = captor.getValue();
        assertEquals(4, updated.getReserved());
        assertEquals(15 - 4 - 2, updated.getAvailable());
        assertEquals(7L, updated.getVersion());
        verify(processEventsPort, times(1)).save(any(ProcessEvents.class));
    }

    @Test
    void testOnHandUpdateInventory() {
        Map<String, Object> m = new HashMap<>();
        m.put("eventId", "evt6");
        m.put("sku", "sku5");
        m.put("qty", 2);
        m.put("version", 8L);
        m.put("onHand", 50);
        InventoryCache cache = new InventoryCache();
        cache.setReserved(5);
        cache.setCommitted(3);
        cache.setOnHand(20);
        cache.setVersion(7L);
        when(processEventsPort.existEventId("evt6")).thenReturn(false);
        when(inventoryCachePort.findBySku("sku5")).thenReturn(cache);
        useCase.executeUpdateInventory(m, Topics.RK_RESERVED);
        ArgumentCaptor<InventoryCache> captor = ArgumentCaptor.forClass(InventoryCache.class);
        verify(inventoryCachePort, times(1)).save(captor.capture());
        InventoryCache updated = captor.getValue();
        assertEquals(50, updated.getOnHand());
        assertEquals(8L, updated.getVersion());
        assertEquals(50 - 7 - 3, updated.getAvailable());
        verify(processEventsPort, times(1)).save(any(ProcessEvents.class));
    }

    @Test
    void testUpdateItemVersionLowerOrEqual() {
        Map<String, Object> m = new HashMap<>();
        m.put("sku", "sku7");
        m.put("onHand", 100);
        m.put("reserved", 10);
        m.put("committed", 5);
        m.put("available", 85);
        m.put("version", 2L);
        InventoryCache cache = new InventoryCache();
        cache.setVersion(3L);
        when(inventoryCachePort.findBySku("sku7")).thenReturn(cache);
        useCase.executeUpdateItem(m);
        verify(inventoryCachePort, never()).save(any());
    }

    @Test
    void testUpdateItemVersionGreater() {
        Map<String, Object> m = new HashMap<>();
        m.put("sku", "sku8");
        m.put("onHand", 200);
        m.put("reserved", 20);
        m.put("committed", 10);
        m.put("available", 170);
        m.put("version", 5L);
        InventoryCache cache = new InventoryCache();
        cache.setVersion(3L);
        when(inventoryCachePort.findBySku("sku8")).thenReturn(cache);
        useCase.executeUpdateItem(m);
        ArgumentCaptor<InventoryCache> captor = ArgumentCaptor.forClass(InventoryCache.class);
        verify(inventoryCachePort, times(1)).save(captor.capture());
        InventoryCache updated = captor.getValue();
        assertEquals(200, updated.getOnHand());
        assertEquals(20, updated.getReserved());
        assertEquals(10, updated.getCommitted());
        assertEquals(170, updated.getAvailable());
        assertEquals(5L, updated.getVersion());
        assertNotNull(updated.getUpdatedAt());
    }
}
