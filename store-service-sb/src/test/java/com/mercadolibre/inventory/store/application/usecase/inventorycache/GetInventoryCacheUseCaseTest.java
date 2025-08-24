package com.mercadolibre.inventory.store.application.usecase.inventorycache;

import com.mercadolibre.inventory.store.domain.model.InventoryCache;
import com.mercadolibre.inventory.store.domain.ports.InventoryCachePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetInventoryCacheUseCaseTest {
    @Mock
    private InventoryCachePort inventoryCachePort;
    private GetInventoryCacheUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new GetInventoryCacheUseCase(inventoryCachePort);
    }

    @Test
    void testExecuteFound() {
        InventoryCache cache = new InventoryCache();
        cache.setSku("sku1");
        when(inventoryCachePort.findBySku("sku1")).thenReturn(cache);
        Optional<InventoryCache> result = useCase.execute("sku1");
        assertTrue(result.isPresent());
        assertEquals("sku1", result.get().getSku());
    }

    @Test
    void testExecuteNotFound() {
        when(inventoryCachePort.findBySku("sku2")).thenReturn(null);
        Optional<InventoryCache> result = useCase.execute("sku2");
        assertFalse(result.isPresent());
    }
}

