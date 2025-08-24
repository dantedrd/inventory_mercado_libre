package com.mercadolibre.inventory.central.infrastructure.config;

import com.mercadolibre.inventory.central.infrastructure.adapters.out.persistence.Item.ItemEntity;
import com.mercadolibre.inventory.central.infrastructure.adapters.out.persistence.Item.SpringItemJpa;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.OffsetDateTime;

@Configuration
public class InitData {
  @Bean
  CommandLineRunner seed(SpringItemJpa items){
    return args -> {
      items.findById("SKU-001").orElseGet(() -> {
        ItemEntity i = new ItemEntity();
        i.setSku("SKU-001");
        i.setOnHand(10);
        i.setReserved(0);
        i.setCommitted(0);
        i.setVersion(0);
        i.setUpdatedAt(OffsetDateTime.now());
        return items.save(i);
      });
    };
  }
}
//onHand - reserved - committed;