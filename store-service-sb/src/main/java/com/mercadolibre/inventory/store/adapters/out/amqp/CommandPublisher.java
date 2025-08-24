package com.mercadolibre.inventory.store.adapters.out.amqp;

import org.springframework.stereotype.Component;

@Component
public class CommandPublisher {
  /*private final RabbitTemplate rabbit;
  public CommandPublisher(RabbitTemplate rabbit){ this.rabbit = rabbit; }

  public void send(String routingKey, String json) {
    MessageProperties props = new MessageProperties();
    props.setContentType("application/json");
    Message msg = new Message(json.getBytes(StandardCharsets.UTF_8), props);
    rabbit.convertAndSend(Topics.EXCHANGE, routingKey, msg);
  }*/
}
