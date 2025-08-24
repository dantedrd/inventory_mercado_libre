package com.mercadolibre.inventory.store.infrastructure.adapters.out.rabbitmq;

import com.mercadolibre.inventory.shared.Topics;
import com.mercadolibre.inventory.store.domain.model.Cancel;
import com.mercadolibre.inventory.store.domain.model.Commit;
import com.mercadolibre.inventory.store.domain.model.Reserve;
import org.springframework.amqp.core.Message;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

import org.springframework.amqp.core.MessageProperties;

import com.mercadolibre.inventory.store.domain.ports.CommandPublisherPort;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class RabbitCommandPublisher implements CommandPublisherPort {

    private final RabbitTemplate rabbit; private final ObjectMapper om = new ObjectMapper();
    public RabbitCommandPublisher(RabbitTemplate rabbit){ this.rabbit = rabbit; }

    private void send(String rk, String json){
        MessageProperties props = new MessageProperties();
        props.setContentType("application/json");
        rabbit.convertAndSend(Topics.EXCHANGE, rk, new Message(json.getBytes(StandardCharsets.UTF_8), props));
    }


    @Override
    public void publishReserve(Reserve c) {
        try {
            String json = om.writeValueAsString(Map.of(
                "sku", c.getSku(),
                "siteId", c.getSiteId(),
                "reservationId", c.getReservationId(),
                "qty", c.getQty()
            ));
            send(Topics.RK_CMD_RESERVE, json);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void publishCommit(Commit cmd) {
        try {
            String json = om.writeValueAsString(Map.of(
                "sku", cmd.getSku(),
                "reservationId", cmd.getReservationId()
            ));
            send(Topics.RK_CMD_COMMIT, json);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void publishCancel(Cancel cmd) {
        try {
            String json = om.writeValueAsString(Map.of(
                "sku", cmd.getSku(),
                "reservationId", cmd.getReservationId(),
                "reason", cmd.getReason()
            ));
            send(Topics.RK_CMD_CANCEL, json);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
