package com.mercadolibre.inventory.central.application.usecase;

import com.mercadolibre.inventory.shared.Topics;
import com.mercadolibre.inventory.shared.exception.CustomException;
import com.mercadolibre.inventory.shared.exception.NotFoundException;
import com.mercadolibre.inventory.central.domain.model.Item;
import com.mercadolibre.inventory.central.domain.model.Reservation;
import com.mercadolibre.inventory.central.domain.model.ReservationState;
import com.mercadolibre.inventory.central.domain.ports.ItemRepositoryPort;
import com.mercadolibre.inventory.central.domain.ports.OutboxPort;
import com.mercadolibre.inventory.central.domain.ports.ReservationRepositoryPort;

import java.time.OffsetDateTime;
import java.util.UUID;

import static com.mercadolibre.inventory.shared.exception.SPError.INSUFFICIENT_STOCK;
import static com.mercadolibre.inventory.shared.exception.SPError.ITEM_NOT_FOUND;


public class ReserveUseCase {
  private final ItemRepositoryPort items;
  private final ReservationRepositoryPort reservations;
  private final OutboxPort outbox;

  public ReserveUseCase(ItemRepositoryPort items, ReservationRepositoryPort reservations, OutboxPort outbox) {
    this.items = items; this.reservations = reservations; this.outbox = outbox;
  }


  public void execute(String sku, String siteId, String reservationId, int qty) {
    Item item = items.findForUpdate(sku)
            .orElseThrow(() -> new NotFoundException(ITEM_NOT_FOUND.getErrorCode(),ITEM_NOT_FOUND.getErrorMessage()));
    if (item.getAvailable() < qty) {
      outbox.enqueue(Topics.RK_RESERVE_REJECTED,
        String.format("{'sku':'%s','reservationId':'%s','qty':%d,'reason':'INSUFFICIENT_STOCK'}", sku, reservationId, qty));

      throw new CustomException(INSUFFICIENT_STOCK.getErrorCode(),INSUFFICIENT_STOCK.getErrorMessage());
    }
    item.setReserved(item.getReserved() + qty);
    item.setVersion(item.getVersion() + 1);
    item.setUpdatedAt(OffsetDateTime.now());
    items.save(item);

    Reservation reservation = new Reservation();
    reservation.setReservationId(reservationId);
    reservation.setSku(sku);
    reservation.setSiteId(siteId);
    reservation.setQty(qty);
    reservation.setState(ReservationState.RESERVED);
    reservation.setCreatedAt(OffsetDateTime.now());
    reservation.setUpdatedAt(OffsetDateTime.now());
    reservations.save(reservation);

    outbox.enqueue(Topics.RK_RESERVED,
      String.format("{'eventId':'%s','sku':'%s','reservationId':'%s','qty':%d,'siteId':'%s','version':%d}",
              UUID.randomUUID(),sku, reservationId, qty, siteId,item.getVersion()));
  }
}
