package com.mercadolibre.inventory.store.infrastructure.adapters.in.sync;

import com.mercadolibre.inventory.shared.commands.CancelCommand;
import com.mercadolibre.inventory.shared.commands.CommitCommand;
import com.mercadolibre.inventory.shared.commands.ReserveCommand;
import com.mercadolibre.inventory.shared.models.GenericResponse;
import com.mercadolibre.inventory.store.application.usecase.SendCancelCommandUseCase;
import com.mercadolibre.inventory.store.application.usecase.SendCommitCommandUseCase;
import com.mercadolibre.inventory.store.application.usecase.SendReserveCommandUseCase;
import com.mercadolibre.inventory.store.domain.model.Cancel;
import com.mercadolibre.inventory.store.domain.model.Commit;
import com.mercadolibre.inventory.store.domain.model.Reserve;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommandControllerTest {
    @Mock
    private SendReserveCommandUseCase reserveUseCase;
    @Mock
    private SendCommitCommandUseCase commitUseCase;
    @Mock
    private SendCancelCommandUseCase cancelUseCase;
    private CommandController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new CommandController(reserveUseCase, commitUseCase, cancelUseCase);
    }

    @Test
    void testReserveEndpoint() {
        ReserveCommand req = new ReserveCommand("sku1", "site1", "res1", 10);
        ResponseEntity<?> response = controller.reserve(req);
        ArgumentCaptor<Reserve> captor = ArgumentCaptor.forClass(Reserve.class);
        verify(reserveUseCase, times(1)).execute(captor.capture());
        Reserve called = captor.getValue();
        assertEquals("sku1", called.getSku());
        assertEquals("site1", called.getSiteId());
        assertEquals("res1", called.getReservationId());
        assertEquals(10, called.getQty());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof GenericResponse);
    }

    @Test
    void testCommitEndpoint() {
        CommitCommand req = new CommitCommand("sku2", "res2");
        ResponseEntity<?> response = controller.commit(req);
        ArgumentCaptor<Commit> captor = ArgumentCaptor.forClass(Commit.class);
        verify(commitUseCase, times(1)).execute(captor.capture());
        Commit called = captor.getValue();
        assertEquals("sku2", called.getSku());
        assertEquals("res2", called.getReservationId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof GenericResponse);
    }

    @Test
    void testCancelEndpoint() {
        CancelCommand req = new CancelCommand("sku3", "res3", "reason");
        ResponseEntity<?> response = controller.cancel(req);
        ArgumentCaptor<Cancel> captor = ArgumentCaptor.forClass(Cancel.class);
        verify(cancelUseCase, times(1)).execute(captor.capture());
        Cancel called = captor.getValue();
        assertEquals("sku3", called.getSku());
        assertEquals("res3", called.getReservationId());
        assertEquals("reason", called.getReason());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof GenericResponse);
    }
}

