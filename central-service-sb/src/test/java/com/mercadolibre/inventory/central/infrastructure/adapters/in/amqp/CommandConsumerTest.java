package com.mercadolibre.inventory.central.infrastructure.adapters.in.amqp;

import com.mercadolibre.inventory.central.application.usecase.CommitUseCase;
import com.mercadolibre.inventory.central.application.usecase.ReserveUseCase;
import com.mercadolibre.inventory.central.application.usecase.CancelUseCase;
import com.mercadolibre.inventory.shared.Topics;
import com.mercadolibre.inventory.shared.exception.CustomException;
import com.mercadolibre.inventory.shared.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class CommandConsumerTest {
    @Mock
    private ReserveUseCase reserveUseCase;
    @Mock
    private CommitUseCase commitUseCase;
    @Mock
    private CancelUseCase cancelUseCase;
    private CommandConsumer consumer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        consumer = new CommandConsumer(reserveUseCase, commitUseCase, cancelUseCase);
    }

    @Test
    void testOnCommandReserveSuccess() throws Exception {
        String json = "{\"sku\":\"sku1\",\"siteId\":\"siteA\",\"reservationId\":\"res1\",\"qty\":5}";
        byte[] body = json.getBytes();
        consumer.onCommand(body, Topics.RK_CMD_RESERVE);
        verify(reserveUseCase, times(1)).execute("sku1", "siteA", "res1", 5);
    }

    @Test
    void testOnCommandReserveCustomException() throws Exception {
        String json = "{\"sku\":\"sku2\",\"siteId\":\"siteB\",\"reservationId\":\"res2\",\"qty\":10}";
        byte[] body = json.getBytes();
        doThrow(new CustomException(1,"stock insuficiente")).when(reserveUseCase).execute("sku2", "siteB", "res2", 10);
        consumer.onCommand(body, Topics.RK_CMD_RESERVE);
        verify(reserveUseCase, times(1)).execute("sku2", "siteB", "res2", 10);
        // Aquí podrías usar un capturador de logs si lo deseas
    }

    @Test
    void testOnCommandReserveNotFoundException() throws Exception {
        String json = "{\"sku\":\"sku3\",\"siteId\":\"siteC\",\"reservationId\":\"res3\",\"qty\":15}";
        byte[] body = json.getBytes();
        doThrow(new NotFoundException(2,"item no encontrado")).when(reserveUseCase).execute("sku3", "siteC", "res3", 15);
        consumer.onCommand(body, Topics.RK_CMD_RESERVE);
        verify(reserveUseCase, times(1)).execute("sku3", "siteC", "res3", 15);
        // Aquí podrías usar un capturador de logs si lo deseas
    }

    @Test
    void testOnCommandCommit() throws Exception {
        String json = "{\"sku\":\"sku4\",\"reservationId\":\"res4\"}";
        byte[] body = json.getBytes();
        consumer.onCommand(body, Topics.RK_CMD_COMMIT);
        verify(commitUseCase, times(1)).execute("sku4", "res4");
    }

    @Test
    void testOnCommandCancelWithReason() throws Exception {
        String json = "{\"sku\":\"sku5\",\"reservationId\":\"res5\",\"reason\":\"cancelado por usuario\"}";
        byte[] body = json.getBytes();
        consumer.onCommand(body, Topics.RK_CMD_CANCEL);
        verify(cancelUseCase, times(1)).execute("sku5", "res5", "cancelado por usuario");
    }

    @Test
    void testOnCommandCancelWithoutReason() throws Exception {
        String json = "{\"sku\":\"sku6\",\"reservationId\":\"res6\"}";
        byte[] body = json.getBytes();
        consumer.onCommand(body, Topics.RK_CMD_CANCEL);
        verify(cancelUseCase, times(1)).execute("sku6", "res6", null);
    }
}

