package com.mercadolibre.inventory.store.application.usecase;

import com.mercadolibre.inventory.store.domain.model.Reserve;
import com.mercadolibre.inventory.store.domain.ports.CommandPublisherPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class SendReserveCommandUseCaseTest {
    @Mock
    private CommandPublisherPort publisher;
    private SendReserveCommandUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new SendReserveCommandUseCase(publisher);
    }

    @Test
    void testExecuteCallsPublisher() {
        Reserve reserve = new Reserve("id123", "productoABC", "r45", 10);
        useCase.execute(reserve);
        verify(publisher, times(1)).publishReserve(reserve);
    }
}

