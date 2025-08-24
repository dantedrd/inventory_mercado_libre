package com.mercadolibre.inventory.central.infrastructure.adapters.out.persistence.Reservation;

import com.mercadolibre.inventory.central.domain.model.ReservationState;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;


@Entity
@Table(name = "reservations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationEntity {
    @Id
    private String reservationId;
    private String sku;
    private String siteId;
    private int qty;
    @Enumerated(EnumType.STRING)
    private ReservationState state;
    private OffsetDateTime expiresAt;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
