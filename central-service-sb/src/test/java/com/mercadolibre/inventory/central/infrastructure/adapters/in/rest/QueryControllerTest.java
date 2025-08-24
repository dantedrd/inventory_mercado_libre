package com.mercadolibre.inventory.central.infrastructure.adapters.in.rest;

import com.mercadolibre.inventory.central.application.usecase.reservation.GetReservationUseCase;
import com.mercadolibre.inventory.central.application.usecase.reservation.GetReservationsUseCase;
import com.mercadolibre.inventory.central.domain.model.Reservation;
import com.mercadolibre.inventory.central.domain.model.ReservationPageable;
import com.mercadolibre.inventory.central.domain.model.ReservationState;
import com.mercadolibre.inventory.shared.models.GenericResponse;
import com.mercadolibre.inventory.shared.models.PageableRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class QueryControllerTest {
    @Mock
    private GetReservationUseCase getReservationUseCase;
    @Mock
    private GetReservationsUseCase getReservationsUseCase;
    private QueryController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new QueryController(getReservationUseCase, getReservationsUseCase);
    }

    @Test
    void testGetReservationFound() {
        Reservation reservation = new Reservation();
        when(getReservationUseCase.execute("sku1")).thenReturn(Optional.of(reservation));
        Object result = controller.get("sku1");
        assertTrue(result instanceof Optional);
        Optional<?> opt = (Optional<?>) result;
        assertTrue(opt.isPresent());
        assertEquals(reservation, opt.get());
    }

    @Test
    void testGetReservationNotFound() {
        when(getReservationUseCase.execute("sku2")).thenReturn(Optional.empty());
        Object result = controller.get("sku2");
        assertTrue(result instanceof Optional);
        Optional<?> opt = (Optional<?>) result;
        assertTrue(opt.isEmpty());
    }

    @Test
    void testListReservations() {
        PageableRequest pageableRequest = PageableRequest.builder().page(0).size(2).sortBy("createdAt").build();
        ReservationPageable reservationPageable = ReservationPageable
                .builder()
                .size(1)
                .number(10)
                .build();
        when(getReservationsUseCase.execute(Optional.of("sku1"), Optional.of(ReservationState.RESERVED), pageableRequest)).thenReturn(reservationPageable);
        ResponseEntity<?> response = controller.listReservations(Optional.of("sku1"), Optional.of(ReservationState.RESERVED), 0, 2);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof GenericResponse);
        GenericResponse<?> body = (GenericResponse<?>) response.getBody();
        assertEquals("reservaciones encontrados correctamente", body.getMessage());
    }
}

