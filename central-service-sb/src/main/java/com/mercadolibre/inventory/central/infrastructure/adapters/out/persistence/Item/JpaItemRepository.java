package com.mercadolibre.inventory.central.infrastructure.adapters.out.persistence.Item;

import com.mercadolibre.inventory.central.domain.model.Item;
import com.mercadolibre.inventory.central.domain.ports.ItemRepositoryPort;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class JpaItemRepository implements ItemRepositoryPort {
  private final SpringItemJpa jpa;

  public JpaItemRepository(SpringItemJpa jpa){
      this.jpa = jpa;

  }

  @Transactional
  public Optional<Item> findForUpdate(String sku){
      Item item1=jpa.findForUpdate(sku)
                .map(itemEntity -> {
                    System.out.println("Item found for update: " + itemEntity);
                    Item item=ItemTransformer.toDomain(itemEntity);
                    item.setAvailable(itemEntity.getAvailable());
                    return item;
                }).orElseGet(() -> {
                    System.out.println("Item not found for SKU: " + sku);
                    return Optional.<Item>empty().orElse(null);
                });
      return Optional.ofNullable(item1);
  }

  @Transactional
  public Item save(Item i){
      ItemEntity entity = ItemTransformer.toEntity(i);
      ItemEntity saved = jpa.save(entity);
      return ItemTransformer.toDomain(saved);
  }

  @Transactional
  public Optional<Item> findById(String sku){
      return jpa.findForUpdate(sku)
                .map(ItemTransformer::toDomain);
  }
}
