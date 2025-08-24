package com.mercadolibre.inventory.central.infrastructure.adapters.out.persistence.Item;

import com.mercadolibre.inventory.central.domain.model.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JpaItemRepositoryTest {
    @Mock
    private SpringItemJpa jpa;
    private JpaItemRepository repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        repository = new JpaItemRepository(jpa);
    }

    @Test
    void testFindForUpdateFound() {
        ItemEntity entity = new ItemEntity();
        entity.setOnHand(60);
        entity.setCommitted(20);
        entity.setReserved(30);
        Item item = new Item();
        when(jpa.findForUpdate("sku1")).thenReturn(Optional.of(entity));
        try (var transformerMock = Mockito.mockStatic(ItemTransformer.class)) {
            transformerMock.when(() -> ItemTransformer.toDomain(entity)).thenReturn(item);
            Optional<Item> result = repository.findForUpdate("sku1");
            assertTrue(result.isPresent());
            assertEquals(item, result.get());
            assertEquals(10, result.get().getAvailable());
        }
    }

    @Test
    void testFindForUpdateNotFound() {
        when(jpa.findForUpdate("sku2")).thenReturn(Optional.empty());
        Optional<Item> result = repository.findForUpdate("sku2");
        assertFalse(result.isPresent());
    }

    @Test
    void testSave() {
        Item item = new Item();
        ItemEntity entity = new ItemEntity();
        ItemEntity savedEntity = new ItemEntity();
        when(jpa.save(any())).thenReturn(savedEntity);
        try (var transformerMock = Mockito.mockStatic(ItemTransformer.class)) {
            transformerMock.when(() -> ItemTransformer.toEntity(item)).thenReturn(entity);
            transformerMock.when(() -> ItemTransformer.toDomain(savedEntity)).thenReturn(item);
            Item result = repository.save(item);
            assertEquals(item, result);
        }
    }

    @Test
    void testFindByIdFound() {
        ItemEntity entity = new ItemEntity();
        Item item = new Item();
        when(jpa.findForUpdate("sku3")).thenReturn(Optional.of(entity));
        try (var transformerMock = Mockito.mockStatic(ItemTransformer.class)) {
            transformerMock.when(() -> ItemTransformer.toDomain(entity)).thenReturn(item);
            Optional<Item> result = repository.findById("sku3");
            assertTrue(result.isPresent());
            assertEquals(item, result.get());
        }
    }

    @Test
    void testFindByIdNotFound() {
        when(jpa.findForUpdate("sku4")).thenReturn(Optional.empty());
        Optional<Item> result = repository.findById("sku4");
        assertFalse(result.isPresent());
    }
}

