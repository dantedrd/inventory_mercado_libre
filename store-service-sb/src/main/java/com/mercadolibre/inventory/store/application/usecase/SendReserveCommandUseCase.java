package com.mercadolibre.inventory.store.application.usecase;


import com.mercadolibre.inventory.store.domain.model.Reserve;
import com.mercadolibre.inventory.store.domain.ports.CommandPublisherPort;

public class SendReserveCommandUseCase {
    private final CommandPublisherPort publisher;

    public SendReserveCommandUseCase(CommandPublisherPort publisher){
        this.publisher = publisher;
    }
    public void execute(Reserve reserve){
        publisher.publishReserve(reserve);
    }
}
