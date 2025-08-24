package com.mercadolibre.inventory.central.infrastructure.adapters.out.persistence.Reservation;

import com.mercadolibre.inventory.central.domain.model.Reservation;

public class ReservationTransformer {

    public static ReservationEntity toEntity(Reservation reservation) {
        if (reservation == null) return null;
        return ReservationEntity.builder()
                .reservationId(reservation.getReservationId())
                .sku(reservation.getSku())
                .siteId(reservation.getSiteId())
                .qty(reservation.getQty())
                .state(reservation.getState())
                .expiresAt(reservation.getExpiresAt())
                .createdAt(reservation.getCreatedAt())
                .updatedAt(reservation.getUpdatedAt())
                .build();
    }

    public static Reservation toDomain(ReservationEntity entity) {
        if (entity == null) return null;
        return Reservation.builder()
                .reservationId(entity.getReservationId())
                .sku(entity.getSku())
                .siteId(entity.getSiteId())
                .qty(entity.getQty())
                .state(entity.getState())
                .expiresAt(entity.getExpiresAt())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
