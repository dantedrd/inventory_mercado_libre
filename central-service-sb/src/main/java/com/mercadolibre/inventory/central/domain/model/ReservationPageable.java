package com.mercadolibre.inventory.central.domain.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationPageable {
    int number;
    int size;
    int totalPages;
    List<Reservation> reservations;
}
