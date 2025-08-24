package com.mercadolibre.inventory.central.application.usecase;

import com.mercadolibre.inventory.shared.Topics;
import com.mercadolibre.inventory.central.domain.model.Item;
import com.mercadolibre.inventory.central.domain.model.Reservation;
import com.mercadolibre.inventory.central.domain.model.ReservationState;
import com.mercadolibre.inventory.central.domain.ports.ItemRepositoryPort;
import com.mercadolibre.inventory.central.domain.ports.OutboxPort;
import com.mercadolibre.inventory.central.domain.ports.ReservationRepositoryPort;

import java.time.OffsetDateTime;
import java.util.UUID;


public class CancelUseCase {
  private final ItemRepositoryPort items;
  private final ReservationRepositoryPort reservations;
  private final OutboxPort outbox;

  public CancelUseCase(ItemRepositoryPort items, ReservationRepositoryPort reservations, OutboxPort outbox) {
    this.items = items; this.reservations = reservations; this.outbox = outbox;
  }


  public void execute(String sku, String reservationId, String reason) {
    Reservation r = reservations.findForUpdate(reservationId).orElseThrow();
    if (r.getState() != ReservationState.RESERVED) return;
    Item item = items.findForUpdate(sku).orElseThrow();

    item.setReserved(item.getReserved() - r.getQty());
    item.setVersion(item.getVersion() + 1);
    item.setUpdatedAt(OffsetDateTime.now());
    items.save(item);

    r.setState(ReservationState.CANCELLED);
    r.setUpdatedAt(OffsetDateTime.now());
    reservations.save(r);

    outbox.enqueue(Topics.RK_CANCELLED,
      String.format("{'eventId':'%s','sku':'%s','reservationId':'%s','qty':%d,'reason':'%s','siteId':'%s','version':'%d'}",
              UUID.randomUUID(),sku, reservationId, r.getQty(), reason == null ? "" : reason, r.getSiteId(), item.getVersion()));
  }
}
