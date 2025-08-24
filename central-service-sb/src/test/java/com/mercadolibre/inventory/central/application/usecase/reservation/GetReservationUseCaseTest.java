package com.mercadolibre.inventory.central.application.usecase.reservation;

import com.mercadolibre.inventory.central.domain.model.Reservation;
import com.mercadolibre.inventory.central.domain.ports.ReservationRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetReservationUseCaseTest {
    @Mock
    private ReservationRepositoryPort reservations;
    private GetReservationUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new GetReservationUseCase(reservations);
    }

    @Test
    void testExecuteFound() {
        Reservation reservation = new Reservation();
        when(reservations.findForUpdate("res1")).thenReturn(Optional.of(reservation));
        Optional<Reservation> result = useCase.execute("res1");
        assertTrue(result.isPresent());
        assertEquals(reservation, result.get());
        verify(reservations, times(1)).findForUpdate("res1");
    }

    @Test
    void testExecuteNotFound() {
        when(reservations.findForUpdate("res2")).thenReturn(Optional.empty());
        Optional<Reservation> result = useCase.execute("res2");
        assertFalse(result.isPresent());
        verify(reservations, times(1)).findForUpdate("res2");
    }
}

