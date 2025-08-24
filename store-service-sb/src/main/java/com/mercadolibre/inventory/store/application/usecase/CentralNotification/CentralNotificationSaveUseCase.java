package com.mercadolibre.inventory.store.application.usecase.CentralNotification;

import com.mercadolibre.inventory.store.domain.model.InventoryCache;
import com.mercadolibre.inventory.store.domain.model.ProcessEvents;
import com.mercadolibre.inventory.store.domain.ports.InventoryCachePort;
import com.mercadolibre.inventory.store.domain.ports.ProcessEventsPort;

import java.time.OffsetDateTime;
import java.util.Map;

import static com.mercadolibre.inventory.shared.Topics.*;

public class CentralNotificationSaveUseCase {
    private final InventoryCachePort inventoryCachePort;
    private final ProcessEventsPort processEventsPort;

    public CentralNotificationSaveUseCase(InventoryCachePort inventoryCachePort, ProcessEventsPort processEventsPort){
        this.inventoryCachePort = inventoryCachePort;
        this.processEventsPort = processEventsPort;
    }

    public void executeUpdateItem(Map<?,?> m){
        String sku = (String) m.get("sku");
        int onHand    = m.get("onHand")    != null ? ((Number)m.get("onHand")).intValue()    : 0;
        int reserved  = m.get("reserved")  != null ? ((Number)m.get("reserved")).intValue()  : 0;
        int committed = m.get("committed") != null ? ((Number)m.get("committed")).intValue() : 0;
        int available = m.get("available") != null ? ((Number)m.get("available")).intValue() : (onHand - reserved - committed);
        Long version  = m.get("version")   != null ? ((Number)m.get("version")).longValue()  : null;
        InventoryCache c = inventoryCachePort.findBySku(sku);

        if (version != null && c.getVersion()!=null && version <= c.getVersion()) return;

        c.setOnHand(onHand); c.setReserved(reserved); c.setCommitted(committed);
        c.setAvailable(available); c.setVersion(version);
        c.setUpdatedAt(OffsetDateTime.now());
        inventoryCachePort.save(c);

    }

    public void executeUpdateInventory(Map<?,?> m,String routingKey){
        String eventId = (String) m.get("eventId");
        if (eventId != null && processEventsPort.existEventId(eventId)) return;

        String sku = (String) m.get("sku");
        Integer qty = m.get("qty") == null ? 0 : ((Number)m.get("qty")).intValue();
        Long version = m.get("version") == null ? null : ((Number)m.get("version")).longValue();

        InventoryCache inventoryCache=this.inventoryCachePort.findBySku(sku);

        if (version != null && inventoryCache.getVersion() != null && version <= inventoryCache.getVersion()) {
            if (eventId != null) {
                var pe = new ProcessEvents();
                pe.setEventId(eventId);
                this.processEventsPort.save(pe);
            }
            return;
        }

        switch (routingKey) {
            case RK_RESERVED -> inventoryCache.setReserved(inventoryCache.getReserved() + qty);
            case RK_COMMITTED -> {
                inventoryCache.setReserved(inventoryCache.getReserved() - qty);
                inventoryCache.setCommitted(inventoryCache.getCommitted() + qty);
            }
            case RK_CMD_CANCEL -> inventoryCache.setReserved(inventoryCache.getReserved() - qty);
        }

        if (m.get("onHand") != null)
            inventoryCache.setOnHand(((Number)m.get("onHand")).intValue());

        inventoryCache.setAvailable(inventoryCache.getOnHand() - inventoryCache.getReserved() - inventoryCache.getCommitted());
        if (version != null) inventoryCache.setVersion(version);
        inventoryCache.setUpdatedAt(OffsetDateTime.now());
        this.inventoryCachePort.save(inventoryCache);

        if (eventId != null) {
            ProcessEvents pe = new ProcessEvents();
            pe.setEventId(eventId);
            this.processEventsPort.save(pe);
        }
    }
}
