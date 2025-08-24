package com.mercadolibre.inventory.store.application.usecase;

import com.mercadolibre.inventory.store.domain.model.Commit;
import com.mercadolibre.inventory.store.domain.ports.CommandPublisherPort;

public class SendCommitCommandUseCase {

    private final CommandPublisherPort publisher;

    public SendCommitCommandUseCase(CommandPublisherPort publisher){
        this.publisher = publisher;
    }

    public void execute(Commit commit){
        publisher.publishCommit(commit);
    }
}
