package com.mercadolibre.inventory.central.application.usecase;

import com.mercadolibre.inventory.central.domain.model.Item;
import com.mercadolibre.inventory.central.domain.ports.ItemRepositoryPort;
import com.mercadolibre.inventory.central.domain.ports.OutboxPort;
import com.mercadolibre.inventory.shared.Topics;

import java.time.OffsetDateTime;
import java.util.UUID;


public class RegisterItemUseCase {
    private final ItemRepositoryPort items;
    private final OutboxPort outbox;

    public RegisterItemUseCase(ItemRepositoryPort items, OutboxPort outbox) {
        this.items = items;
        this.outbox = outbox;
    }

    public Item execute(Item item) {
        Item itemSaved= items.findById(item.getSku())
                .map(i -> {
                    i.setOnHand(item.getOnHand());
                    i.setVersion(i.getVersion() + 1);
                    i.setUpdatedAt(OffsetDateTime.now());
                    return items.save(i);
                })
                .orElseGet(() -> {
                    item.setReserved(0);
                    item.setCommitted(0);
                    item.setVersion(0);
                    item.setUpdatedAt(OffsetDateTime.now());
                    return items.save(item);
                });
        String payload = """
    {"eventId":"%s","sku":"%s","onHand":%d,"reserved":%d,"committed":%d,"available":%d,"version":%d}
  """.formatted(
                UUID.randomUUID(), itemSaved.getSku(), itemSaved.getOnHand(), itemSaved.getReserved(),
                itemSaved.getCommitted(), itemSaved.getAvailable(), itemSaved.getVersion());

        outbox.enqueue(Topics.RK_ITEM_UPSERTED, payload);

        return  itemSaved;
    }
}
