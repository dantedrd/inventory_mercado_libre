package com.mercadolibre.inventory.central.domain.model;

import lombok.*;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item {

  private String sku;
  private int onHand;
  private int reserved;
  private int committed;
  private int available;
  private long version;
  private OffsetDateTime updatedAt;

}
