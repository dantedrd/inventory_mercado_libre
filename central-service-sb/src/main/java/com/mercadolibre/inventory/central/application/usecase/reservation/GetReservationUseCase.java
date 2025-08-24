package com.mercadolibre.inventory.central.application.usecase.reservation;

import com.mercadolibre.inventory.central.domain.model.Reservation;
import com.mercadolibre.inventory.central.domain.ports.ReservationRepositoryPort;

import java.util.Optional;

public class GetReservationUseCase {
    private final ReservationRepositoryPort reservations;

    public GetReservationUseCase(ReservationRepositoryPort reservations) {
        this.reservations = reservations;
    }

    public  Optional<Reservation> execute(String reservationId) {
        return this.reservations.findForUpdate(reservationId);
    }
}
