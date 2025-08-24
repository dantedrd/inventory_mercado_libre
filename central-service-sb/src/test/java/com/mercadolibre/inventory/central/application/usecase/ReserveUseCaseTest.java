package com.mercadolibre.inventory.central.application.usecase;

import com.mercadolibre.inventory.central.domain.model.Item;
import com.mercadolibre.inventory.central.domain.model.Reservation;
import com.mercadolibre.inventory.central.domain.ports.ItemRepositoryPort;
import com.mercadolibre.inventory.central.domain.ports.OutboxPort;
import com.mercadolibre.inventory.central.domain.ports.ReservationRepositoryPort;
import com.mercadolibre.inventory.shared.exception.CustomException;
import com.mercadolibre.inventory.shared.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReserveUseCaseTest {
    @Mock
    private ItemRepositoryPort items;
    @Mock
    private ReservationRepositoryPort reservations;
    @Mock
    private OutboxPort outbox;
    private ReserveUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new ReserveUseCase(items, reservations, outbox);
    }

    @Test
    void testItemNotFoundThrows() {
        when(items.findForUpdate("sku1")).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> useCase.execute("sku1", "site1", "res1", 10));
    }

    @Test
    void testInsufficientStockThrowsAndEnqueue() {
        Item item = new Item();
        item.setAvailable(5);
        when(items.findForUpdate("sku2")).thenReturn(Optional.of(item));
        assertThrows(CustomException.class, () -> useCase.execute("sku2", "site2", "res2", 10));
        verify(outbox, times(1)).enqueue(any(), contains("sku2"));
    }

    @Test
    void testReserveSuccess() {
        Item item = new Item();
        item.setAvailable(20);
        item.setReserved(5);
        item.setVersion(1L);
        when(items.findForUpdate("sku3")).thenReturn(Optional.of(item));
        when(items.save(any(Item.class))).thenReturn(item);
        Reservation reservation = new Reservation();
        when(reservations.save(any(Reservation.class))).thenReturn(reservation);
        useCase.execute("sku3", "site3", "res3", 10);
        ArgumentCaptor<Item> itemCaptor = ArgumentCaptor.forClass(Item.class);
        ArgumentCaptor<Reservation> resCaptor = ArgumentCaptor.forClass(Reservation.class);
        verify(items, times(1)).save(itemCaptor.capture());
        verify(reservations, times(1)).save(resCaptor.capture());
        Item updatedItem = itemCaptor.getValue();
        Reservation updatedRes = resCaptor.getValue();
        assertEquals(15, updatedItem.getReserved());
        assertEquals(2L, updatedItem.getVersion());
        assertEquals("res3", updatedRes.getReservationId());
        verify(outbox, times(1)).enqueue(any(), contains("sku3"));
    }
}

