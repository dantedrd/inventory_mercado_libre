package com.mercadolibre.inventory.central.application.usecase.reservation;

import com.mercadolibre.inventory.central.domain.model.ReservationPageable;
import com.mercadolibre.inventory.central.domain.model.ReservationState;
import com.mercadolibre.inventory.central.domain.ports.ReservationRepositoryPort;
import com.mercadolibre.inventory.shared.models.PageableRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GetReservationsUseCaseTest {
    @Mock
    private ReservationRepositoryPort reservations;
    private GetReservationsUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new GetReservationsUseCase(reservations);
    }

    @Test
    void testExecuteCallsPort() {
        PageableRequest pageableRequest = PageableRequest.builder().page(0).size(10).sortBy("createdAt").build();
        ReservationPageable expected = mock(ReservationPageable.class);
        when(reservations.listReservations(Optional.of("sku1"), Optional.of(ReservationState.RESERVED), pageableRequest)).thenReturn(expected);
        ReservationPageable result = useCase.execute(Optional.of("sku1"), Optional.of(ReservationState.RESERVED), pageableRequest);
        assertEquals(expected, result);
        verify(reservations, times(1)).listReservations(Optional.of("sku1"), Optional.of(ReservationState.RESERVED), pageableRequest);
    }
}

