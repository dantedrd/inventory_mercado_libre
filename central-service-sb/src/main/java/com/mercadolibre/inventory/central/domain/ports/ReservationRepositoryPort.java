package com.mercadolibre.inventory.central.domain.ports;

import com.mercadolibre.inventory.central.domain.model.Reservation;
import com.mercadolibre.inventory.central.domain.model.ReservationPageable;
import com.mercadolibre.inventory.central.domain.model.ReservationState;
import com.mercadolibre.inventory.shared.models.PageableRequest;

import java.util.Optional;

public interface ReservationRepositoryPort {
  Optional<Reservation> findForUpdate(String id);
  Reservation save(Reservation r);
  ReservationPageable listReservations(Optional<String> sku, Optional<ReservationState> state, PageableRequest pageableRequest);
}
