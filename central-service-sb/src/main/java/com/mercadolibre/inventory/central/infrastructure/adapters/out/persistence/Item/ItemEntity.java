package com.mercadolibre.inventory.central.infrastructure.adapters.out.persistence.Item;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.*;

import java.time.OffsetDateTime;

@Entity
@Table(name = "items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemEntity {
  @Id
  private String sku;
  private int onHand;
  private int reserved;
  private int committed;
  private long version;
  private OffsetDateTime updatedAt;

  @Transient
  public int getAvailable() {
      return onHand - reserved - committed;
  }
}
