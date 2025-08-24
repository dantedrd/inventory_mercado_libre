package com.mercadolibre.inventory.central.domain.ports;
public interface OutboxPort {
  void enqueue(String routingKey, String payloadJson);
}
