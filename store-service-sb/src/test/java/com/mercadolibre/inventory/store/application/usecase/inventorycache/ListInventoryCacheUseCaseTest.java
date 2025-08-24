package com.mercadolibre.inventory.store.application.usecase.inventorycache;

import com.mercadolibre.inventory.store.domain.model.InventoryCache;
import com.mercadolibre.inventory.store.domain.model.InventoryCachePageable;
import com.mercadolibre.inventory.store.domain.ports.InventoryCachePort;
import com.mercadolibre.inventory.shared.models.PageableRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ListInventoryCacheUseCaseTest {
    @Mock
    private InventoryCachePort inventoryCachePort;
    private ListInventoryCacheUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new ListInventoryCacheUseCase(inventoryCachePort);
    }


    @Test
    void testExecutePaged() {
        PageableRequest pageableRequest = PageableRequest.builder().page(0).size(2).sortBy("sku").build();
        InventoryCache cache1 = new InventoryCache(); cache1.setSku("sku1");
        InventoryCache cache2 = new InventoryCache(); cache2.setSku("sku2");
        InventoryCachePageable pageable = new InventoryCachePageable(List.of(cache1, cache2), 0, 2, 2, 1);
        when(inventoryCachePort.listInventoryCaches(pageableRequest)).thenReturn(pageable);
        InventoryCachePageable result = useCase.execute(pageableRequest);
        assertEquals(2, result.getItems().size());
        assertEquals("sku1", result.getItems().get(0).getSku());
        assertEquals("sku2", result.getItems().get(1).getSku());
        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
    }
}

