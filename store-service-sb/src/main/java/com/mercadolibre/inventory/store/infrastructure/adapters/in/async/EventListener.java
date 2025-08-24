package com.mercadolibre.inventory.store.infrastructure.adapters.in.async;

import com.mercadolibre.inventory.store.application.usecase.CentralNotification.CentralNotificationSaveUseCase;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mercadolibre.inventory.store.domain.model.InventoryCache;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@Component
public class EventListener {

    private static final Logger log = LoggerFactory.getLogger(EventListener.class);
    private final CentralNotificationSaveUseCase centralNotificationSaveUseCase;
    private final ObjectMapper om = new ObjectMapper().configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);;

    public EventListener(CentralNotificationSaveUseCase centralNotificationSaveUseCase) {
        this.centralNotificationSaveUseCase = centralNotificationSaveUseCase;
    }


    @RabbitListener(queues = "store.events")
    public void onEvent(byte[] body, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey) throws Exception {
        Map<?,?> event = om.readValue(body, Map.class);
        log.info("store.events");
        log.info(event.toString());
        if("event.item.upserted".equals(routingKey)){
            log.info("actualiza item");
            this.centralNotificationSaveUseCase.executeUpdateItem(event);
            return;
        }
        this.centralNotificationSaveUseCase.executeUpdateInventory(event,routingKey);
    }
}
