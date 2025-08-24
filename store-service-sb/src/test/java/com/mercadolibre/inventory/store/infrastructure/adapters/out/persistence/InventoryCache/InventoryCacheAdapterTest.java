package com.mercadolibre.inventory.store.infrastructure.adapters.out.persistence.InventoryCache;

import com.mercadolibre.inventory.store.domain.model.InventoryCache;
import com.mercadolibre.inventory.store.domain.model.InventoryCachePageable;
import com.mercadolibre.inventory.shared.models.PageableRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InventoryCacheAdapterTest {
    @Mock
    private InventoryCacheJpa jpa;
    @InjectMocks
    private InventoryCacheAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new InventoryCacheAdapter(jpa);
    }

    @Test
    void testSave() {
        InventoryCache cache = new InventoryCache();
        cache.setSku("sku1");
        InventoryCacheEntity entity = new InventoryCacheEntity();
        entity.setSku("sku1");
        when(jpa.save(any())).thenReturn(entity);
        try (var transformerMock = Mockito.mockStatic(InventoryCacheTransformer.class)) {
            transformerMock.when(() -> InventoryCacheTransformer.toEntity(cache)).thenReturn(entity);
            transformerMock.when(() -> InventoryCacheTransformer.toDomain(entity)).thenReturn(cache);
            InventoryCache result = adapter.save(cache);
            assertEquals("sku1", result.getSku());
        }
    }

    @Test
    void testFindBySkuFound() {
        InventoryCacheEntity entity = new InventoryCacheEntity();
        entity.setSku("sku2");
        InventoryCache cache = new InventoryCache();
        cache.setSku("sku2");
        when(jpa.findById("sku2")).thenReturn(Optional.of(entity));
        try (var transformerMock = Mockito.mockStatic(InventoryCacheTransformer.class)) {
            transformerMock.when(() -> InventoryCacheTransformer.toDomain(entity)).thenReturn(cache);
            InventoryCache result = adapter.findBySku("sku2");
            assertEquals("sku2", result.getSku());
        }
    }

    @Test
    void testFindBySkuNotFound() {
        when(jpa.findById("sku3")).thenReturn(Optional.empty());
        InventoryCache result = adapter.findBySku("sku3");
        assertEquals("sku3", result.getSku());
        assertEquals(0, result.getOnHand());
        assertEquals(0, result.getReserved());
        assertEquals(0, result.getCommitted());
        assertEquals(0, result.getAvailable());
        assertNull(result.getVersion());
    }



    @Test
    void testListInventoryCaches() {
        InventoryCacheEntity entity = new InventoryCacheEntity();
        entity.setSku("sku5");
        InventoryCache cache = new InventoryCache();
        cache.setSku("sku5");
        Page<InventoryCacheEntity> page = new PageImpl<>(List.of(entity), PageRequest.of(0, 1), 1);
        when(jpa.findAll(any(PageRequest.class))).thenReturn(page);
        try (var transformerMock = Mockito.mockStatic(InventoryCacheTransformer.class)) {
            transformerMock.when(() -> InventoryCacheTransformer.toDomain(entity)).thenReturn(cache);
            PageableRequest pageableRequest = PageableRequest.builder().page(0).size(1).sortBy("sku").build();
            InventoryCachePageable result = adapter.listInventoryCaches(pageableRequest);
            assertEquals(1, result.getItems().size());
            assertEquals("sku5", result.getItems().get(0).getSku());
            assertEquals(1, result.getTotalElements());
            assertEquals(1, result.getTotalPages());
        }
    }
}
