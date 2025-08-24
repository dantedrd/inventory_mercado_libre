package com.mercadolibre.inventory.store.application.usecase;

import com.mercadolibre.inventory.store.domain.model.Cancel;
import com.mercadolibre.inventory.store.domain.ports.CommandPublisherPort;

public class SendCancelCommandUseCase {
    private final CommandPublisherPort publisher;

    public SendCancelCommandUseCase(CommandPublisherPort publisher){
        this.publisher = publisher;
    }

    public void execute(Cancel cancel){
        publisher.publishCancel(cancel);
    }
}
