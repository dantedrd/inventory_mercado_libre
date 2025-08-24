package com.mercadolibre.inventory.central.application.usecase;

import com.mercadolibre.inventory.central.domain.model.Item;
import com.mercadolibre.inventory.central.domain.ports.ItemRepositoryPort;
import com.mercadolibre.inventory.central.domain.ports.OutboxPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegisterItemUseCaseTest {
    @Mock
    private ItemRepositoryPort items;
    @Mock
    private OutboxPort outbox;
    private RegisterItemUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new RegisterItemUseCase(items, outbox);
    }

    @Test
    void testUpdateExistingItem() {
        Item existing = new Item();
        existing.setSku("sku1");
        existing.setOnHand(10);
        existing.setReserved(5);
        existing.setCommitted(2);
        existing.setVersion(1L);
        Item input = new Item();
        input.setSku("sku1");
        input.setOnHand(20);
        when(items.findById("sku1")).thenReturn(Optional.of(existing));
        when(items.save(any(Item.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Item result = useCase.execute(input);
        assertAll(
            () -> assertEquals(20, existing.getOnHand()),
            () -> assertEquals(2L, existing.getVersion()),
            () -> assertNotNull(existing.getUpdatedAt()),
            () -> assertEquals("sku1", result.getSku()),
            () -> assertEquals(20, result.getOnHand()),
            () -> assertEquals(5, result.getReserved()),
            () -> assertEquals(2, result.getCommitted()),
            () -> assertEquals(2L, result.getVersion()),
            () -> assertNotNull(result.getUpdatedAt())
        );
        verify(items, times(1)).save(existing);
        verify(outbox, times(1)).enqueue(anyString(), anyString());
        assertEquals(existing, result);
    }

    @Test
    void testCreateNewItem() {
        Item input = new Item();
        input.setSku("sku2");
        input.setOnHand(15);
        when(items.findById("sku2")).thenReturn(Optional.empty());
        when(items.save(any(Item.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Item result = useCase.execute(input);
        assertAll(
            () -> assertEquals(0, input.getReserved()),
            () -> assertEquals(0, input.getCommitted()),
            () -> assertEquals(0, input.getVersion()),
            () -> assertNotNull(input.getUpdatedAt()),
            () -> assertEquals("sku2", result.getSku()),
            () -> assertEquals(15, result.getOnHand()),
            () -> assertEquals(0, result.getReserved()),
            () -> assertEquals(0, result.getCommitted()),
            () -> assertEquals(0, result.getVersion()),
            () -> assertNotNull(result.getUpdatedAt())
        );
        verify(items, times(1)).save(input);
        verify(outbox, times(1)).enqueue(anyString(), anyString());
        assertEquals(input, result);
    }
}
