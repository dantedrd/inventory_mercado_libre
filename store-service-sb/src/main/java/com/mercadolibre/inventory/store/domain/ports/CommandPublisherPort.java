package com.mercadolibre.inventory.store.domain.ports;

import com.mercadolibre.inventory.store.domain.model.Cancel;
import com.mercadolibre.inventory.store.domain.model.Commit;
import com.mercadolibre.inventory.store.domain.model.Reserve;

public interface CommandPublisherPort {
    void publishReserve(Reserve cmd);
    void publishCommit(Commit cmd);
    void publishCancel(Cancel cmd);
}
