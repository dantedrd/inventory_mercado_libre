package com.mercadolibre.inventory.central.infrastructure.adapters.in.amqp;

import com.mercadolibre.inventory.shared.Topics;
import com.mercadolibre.inventory.shared.exception.CustomException;
import com.mercadolibre.inventory.shared.exception.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.inventory.central.application.usecase.CancelUseCase;
import com.mercadolibre.inventory.central.application.usecase.CommitUseCase;
import com.mercadolibre.inventory.central.application.usecase.ReserveUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
public class CommandConsumer {

  private final ReserveUseCase reserve;
  private final CommitUseCase commit;
  private final CancelUseCase cancel;
  private final ObjectMapper om = new ObjectMapper();

  private static final Logger logger = LoggerFactory.getLogger(CommandConsumer.class);

  public CommandConsumer(ReserveUseCase r, CommitUseCase c, CancelUseCase k){
    this.reserve = r; this.commit = c; this.cancel = k;
  }

  @RabbitListener(queues = "central.commands")
  public void onCommand(byte[] body, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String rk) throws Exception {
    Map<?,?> m = om.readValue(body, Map.class);
    if (Topics.RK_CMD_RESERVE.equals(rk)) {
        try {
            reserve.execute((String)m.get("sku"), (String)m.get("siteId"), (String)m.get("reservationId"),
                    ((Number)m.get("qty")).intValue());
        } catch (CustomException e) {
            logger.error("el stock es insuficiente para sku: {}, reservationId: {}, qty: {}",
                    (String)m.get("sku"), (String)m.get("reservationId"), ((Number)m.get("qty")).intValue());
        } catch (NotFoundException e) {
            logger.error("el item no fue encontrado para sku: {}, reservationId: {}, qty: {}",
                    (String)m.get("sku"), (String)m.get("reservationId"), ((Number)m.get("qty")).intValue());
        }

    } else if (Topics.RK_CMD_COMMIT.equals(rk)) {
      commit.execute((String)m.get("sku"), (String)m.get("reservationId"));
    } else if (Topics.RK_CMD_CANCEL.equals(rk)) {
      String reason = m.get("reason") == null ? null : (String)m.get("reason");
      cancel.execute((String)m.get("sku"), (String)m.get("reservationId"), reason);
    }
  }
}
