package com.mercadolibre.inventory.central.application.usecase.reservation;

import com.mercadolibre.inventory.central.domain.model.ReservationPageable;
import com.mercadolibre.inventory.central.domain.model.ReservationState;
import com.mercadolibre.inventory.central.domain.ports.ReservationRepositoryPort;
import com.mercadolibre.inventory.shared.models.PageableRequest;

import java.util.Optional;

public class GetReservationsUseCase {
    private final ReservationRepositoryPort reservations;

    public GetReservationsUseCase(ReservationRepositoryPort reservations) {
        this.reservations = reservations;
    }

    public ReservationPageable execute(Optional<String> sku, Optional<ReservationState> state, PageableRequest pageableRequest) {
       return this.reservations.listReservations(sku, state, pageableRequest);
    }
}
