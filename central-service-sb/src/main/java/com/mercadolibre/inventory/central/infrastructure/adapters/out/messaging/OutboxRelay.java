package com.mercadolibre.inventory.central.infrastructure.adapters.out.messaging;

import com.mercadolibre.inventory.central.infrastructure.adapters.out.persistence.OutboxEntity;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class OutboxRelay {
  private final OutboxJpa jpa;
  private final RabbitTemplate rabbit;
  public OutboxRelay(OutboxJpa jpa, RabbitTemplate rabbit){
    this.jpa = jpa; this.rabbit = rabbit;
  }

  @Scheduled(fixedDelay = 500)
  @Transactional
  public void flush() {
    List<OutboxEntity> batch = jpa.findTop50ByStatusOrderByCreatedAtAsc("PENDING");
    for (OutboxEntity e : batch) {
      MessageProperties props = new MessageProperties();
      props.setContentType("application/json");
      Message msg = new Message(e.getPayload().getBytes(StandardCharsets.UTF_8), props);
      rabbit.convertAndSend("inventory", e.getRoutingKey(), msg);
      e.setStatus("SENT");
      jpa.save(e);
    }
  }
}
