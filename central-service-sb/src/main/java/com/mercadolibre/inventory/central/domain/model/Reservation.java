package com.mercadolibre.inventory.central.domain.model;

import java.time.OffsetDateTime;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {

  private String reservationId;
  private String sku;
  private String siteId;
  private int qty;
  private ReservationState state;
  private OffsetDateTime expiresAt;
  private OffsetDateTime createdAt;
  private OffsetDateTime updatedAt;

}
