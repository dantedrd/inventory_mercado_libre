package com.mercadolibre.inventory.central.domain.ports;

import com.mercadolibre.inventory.central.domain.model.Item;
import java.util.Optional;

public interface ItemRepositoryPort {
  Optional<Item> findForUpdate(String sku);
  Item save(Item i);
  Optional<Item> findById(String sku);
}
