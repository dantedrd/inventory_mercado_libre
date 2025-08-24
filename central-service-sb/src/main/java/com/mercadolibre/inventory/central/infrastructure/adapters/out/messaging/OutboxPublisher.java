package com.mercadolibre.inventory.central.infrastructure.adapters.out.messaging;

import com.mercadolibre.inventory.central.domain.ports.OutboxPort;
import com.mercadolibre.inventory.central.infrastructure.adapters.out.persistence.OutboxEntity;
import org.springframework.stereotype.Component;
import java.time.OffsetDateTime;

@Component
public class OutboxPublisher implements OutboxPort {
  private final OutboxJpa jpa;
  public OutboxPublisher(OutboxJpa jpa){ this.jpa = jpa; }
  public void enqueue(String routingKey, String payloadJson){
    OutboxEntity e = new OutboxEntity();
    e.setRoutingKey(routingKey);
    e.setPayload(payloadJson);
    e.setStatus("PENDING");
    e.setCreatedAt(OffsetDateTime.now());
    jpa.save(e);
  }
}
