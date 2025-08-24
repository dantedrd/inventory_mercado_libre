package com.mercadolibre.inventory.store.infrastructure.config;

import com.mercadolibre.inventory.shared.Topics;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean; import org.springframework.context.annotation.Configuration;
@Configuration
public class AmqpTopology {

    @Bean
    TopicExchange inventoryExchange(){
        return new TopicExchange(Topics.EXCHANGE, true, false);
    }

    @Bean
    Queue storeEvents(){
        return QueueBuilder.durable("store.events").build();
    }

    @Bean
    Binding bindEvents(TopicExchange ex, Queue storeEvents){
        return BindingBuilder.bind(storeEvents).to(ex).with("event.inventory.*");
    }

    @Bean Binding bindItems(TopicExchange ex, Queue storeEvents){
        return BindingBuilder.bind(storeEvents).to(ex).with("event.item.*");
    }
}
