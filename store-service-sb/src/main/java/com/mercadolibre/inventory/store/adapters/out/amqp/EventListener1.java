package com.mercadolibre.inventory.store.adapters.out.amqp;

import org.springframework.stereotype.Component;

@Component
public class EventListener1 {
  /*@RabbitListener(queues = "store.events")
  public void onEvent(byte[] body){
    System.out.println("STORE EVENT -> " + new String(body));
  }*/
}
