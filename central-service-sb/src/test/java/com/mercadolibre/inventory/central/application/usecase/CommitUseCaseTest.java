package com.mercadolibre.inventory.central.application.usecase;

import com.mercadolibre.inventory.central.domain.model.Item;
import com.mercadolibre.inventory.central.domain.model.Reservation;
import com.mercadolibre.inventory.central.domain.model.ReservationState;
import com.mercadolibre.inventory.central.domain.ports.ItemRepositoryPort;
import com.mercadolibre.inventory.central.domain.ports.OutboxPort;
import com.mercadolibre.inventory.central.domain.ports.ReservationRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommitUseCaseTest {
    @Mock
    private ItemRepositoryPort items;
    @Mock
    private ReservationRepositoryPort reservations;
    @Mock
    private OutboxPort outbox;
    private CommitUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new CommitUseCase(items, reservations, outbox);
    }

    @Test
    void testReservationNotFoundThrows() {
        when(reservations.findForUpdate("res1")).thenReturn(Optional.empty());
        assertThrows(Exception.class, () -> useCase.execute("sku1", "res1"));
    }

    @Test
    void testReservationNotReservedDoesNothing() {
        Reservation r = new Reservation();
        r.setState(ReservationState.COMMITTED);
        when(reservations.findForUpdate("res2")).thenReturn(Optional.of(r));
        useCase.execute("sku2", "res2");
        verify(items, never()).findForUpdate(any());
        verify(items, never()).save(any());
        verify(reservations, never()).save(any());
        verify(outbox, never()).enqueue(any(), any());
    }

    @Test
    void testCommitSuccess() {
        Reservation r = new Reservation();
        r.setState(ReservationState.RESERVED);
        r.setQty(5);
        r.setReservationId("res3");
        Item item = new Item();
        item.setReserved(10);
        item.setCommitted(2);
        item.setVersion(1L);
        when(reservations.findForUpdate("res3")).thenReturn(Optional.of(r));
        when(items.findForUpdate("sku3")).thenReturn(Optional.of(item));
        useCase.execute("sku3", "res3");
        ArgumentCaptor<Item> itemCaptor = ArgumentCaptor.forClass(Item.class);
        ArgumentCaptor<Reservation> resCaptor = ArgumentCaptor.forClass(Reservation.class);
        verify(items, times(1)).save(itemCaptor.capture());
        verify(reservations, times(1)).save(resCaptor.capture());
        Item updatedItem = itemCaptor.getValue();
        Reservation updatedRes = resCaptor.getValue();
        assertEquals(5, updatedItem.getReserved());
        assertEquals(7, updatedItem.getCommitted());
        assertEquals(2L, updatedItem.getVersion());
        assertEquals(ReservationState.COMMITTED, updatedRes.getState());
        verify(outbox, times(1)).enqueue(any(), contains("sku3"));
    }
}

