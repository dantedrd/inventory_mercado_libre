package com.mercadolibre.inventory.store.application.usecase;

import com.mercadolibre.inventory.store.domain.model.Commit;
import com.mercadolibre.inventory.store.domain.ports.CommandPublisherPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class SendCommitCommandUseCaseTest {
    @Mock
    private CommandPublisherPort publisher;
    private SendCommitCommandUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new SendCommitCommandUseCase(publisher);
    }

    @Test
    void testExecuteCallsPublisher() {
        Commit commit = new Commit();
        useCase.execute(commit);
        verify(publisher, times(1)).publishCommit(commit);
    }
}

