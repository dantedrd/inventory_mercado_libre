package com.mercadolibre.inventory.store.application.usecase;

import com.mercadolibre.inventory.store.domain.model.Cancel;
import com.mercadolibre.inventory.store.domain.ports.CommandPublisherPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class SendCancelCommandUseCaseTest {
    @Mock
    private CommandPublisherPort publisher;
    private SendCancelCommandUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new SendCancelCommandUseCase(publisher);
    }

    @Test
    void testExecuteCallsPublisher() {
        Cancel cancel = new Cancel();
        useCase.execute(cancel);
        verify(publisher, times(1)).publishCancel(cancel);
    }
}

