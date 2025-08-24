package com.mercadolibre.inventory.store.infrastructure.adapters.in.sync;

import com.mercadolibre.inventory.store.application.usecase.inventorycache.GetInventoryCacheUseCase;
import com.mercadolibre.inventory.store.application.usecase.inventorycache.ListInventoryCacheUseCase;
import com.mercadolibre.inventory.store.domain.model.InventoryCache;
import com.mercadolibre.inventory.store.domain.model.InventoryCachePageable;
import com.mercadolibre.inventory.shared.models.GenericResponse;
import com.mercadolibre.inventory.shared.models.PageableRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class QueryCacheControllerTest {
    @Mock
    private GetInventoryCacheUseCase getInventoryCacheUseCase;
    @Mock
    private ListInventoryCacheUseCase listInventoryCacheUseCase;
    private QueryCacheController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new QueryCacheController(getInventoryCacheUseCase, listInventoryCacheUseCase);
    }

    @Test
    void testGetBySkuFound() {
        InventoryCache cache = new InventoryCache();
        cache.setSku("sku1");
        when(getInventoryCacheUseCase.execute("sku1")).thenReturn(Optional.of(cache));
        ResponseEntity<?> response = controller.getBySku("sku1");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GenericResponse<Optional<InventoryCache>> body = (GenericResponse<Optional<InventoryCache>>) response.getBody();
        assertEquals("Inventario encontrado correctamente", body.getMessage());
        assertTrue(!body.getData().get().getSku().isEmpty());
        assertEquals("sku1", body.getData().get().getSku());
    }

    @Test
    void testGetBySkuNotFound() {
        when(getInventoryCacheUseCase.execute("sku2")).thenReturn(Optional.empty());
        ResponseEntity<?> response = controller.getBySku("sku2");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GenericResponse<Optional<InventoryCache>> body = (GenericResponse<Optional<InventoryCache>>) response.getBody();
        assertEquals("Inventario encontrado correctamente", body.getMessage());
        assertTrue(body.getData().isEmpty());
    }

    @Test
    void testListPaged() {
        InventoryCache cache1 = new InventoryCache(); cache1.setSku("sku1");
        InventoryCache cache2 = new InventoryCache(); cache2.setSku("sku2");
        InventoryCachePageable pageable = new InventoryCachePageable(List.of(cache1, cache2), 0, 2, 2, 1);
        when(listInventoryCacheUseCase.execute(any(PageableRequest.class))).thenReturn(pageable);
        ResponseEntity<?> response = controller.listPaged(0, 2, "sku");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        GenericResponse<InventoryCachePageable> body = (GenericResponse<InventoryCachePageable>) response.getBody();
        assertEquals("Inventarios encontrados correctamente", body.getMessage());
        InventoryCachePageable result = (InventoryCachePageable) body.getData();
        assertEquals(2, result.getItems().size());
        assertEquals("sku1", result.getItems().get(0).getSku());
        assertEquals("sku2", result.getItems().get(1).getSku());
    }
}

