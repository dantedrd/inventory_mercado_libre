package com.mercadolibre.inventory.central.infrastructure.config;

import com.mercadolibre.inventory.shared.Topics;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmqpTopology {

  @Bean
  public TopicExchange inventoryExchange() {
    return new TopicExchange(Topics.EXCHANGE, true, false);
  }

  @Bean
  public Queue centralCommands() {
    return QueueBuilder.durable("central.commands").build();
  }

  @Bean
  public Queue storeEvents() {
    return QueueBuilder.durable("store.events").build();
  }

  @Bean
  public Binding bindCmds(TopicExchange ex, Queue centralCommands) {
    return BindingBuilder.bind(centralCommands).to(ex).with("command.inventory.*");
  }

  @Bean
  public Binding bindEvents(TopicExchange ex, Queue storeEvents) {
    return BindingBuilder.bind(storeEvents).to(ex).with("event.inventory.*");
  }
}
