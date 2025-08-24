package com.mercadolibre.inventory.central.infrastructure.adapters.in.rest;

import com.mercadolibre.inventory.central.application.usecase.CancelUseCase;
import com.mercadolibre.inventory.central.application.usecase.CommitUseCase;
import com.mercadolibre.inventory.central.application.usecase.ReserveUseCase;
import com.mercadolibre.inventory.shared.commands.CancelCommand;
import com.mercadolibre.inventory.shared.commands.CommitCommand;
import com.mercadolibre.inventory.shared.commands.ReserveCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommandControllerTest {
    @Mock
    private ReserveUseCase reserveUseCase;
    @Mock
    private CommitUseCase commitUseCase;
    @Mock
    private CancelUseCase cancelUseCase;
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
        verify(reserveUseCase, times(1)).execute("sku1", "site1", "res1", 10);
        assertEquals(202, response.getStatusCodeValue());
    }

    @Test
    void testCommitEndpoint() {
        CommitCommand req = new CommitCommand("sku2", "res2");
        ResponseEntity<?> response = controller.commit(req);
        verify(commitUseCase, times(1)).execute("sku2", "res2");
        assertEquals(202, response.getStatusCodeValue());
    }

    @Test
    void testCancelEndpoint() {
        CancelCommand req = new CancelCommand("sku3", "res3", "reason");
        ResponseEntity<?> response = controller.cancel(req);
        verify(cancelUseCase, times(1)).execute("sku3", "res3", "reason");
        assertEquals(202, response.getStatusCodeValue());
    }
}

