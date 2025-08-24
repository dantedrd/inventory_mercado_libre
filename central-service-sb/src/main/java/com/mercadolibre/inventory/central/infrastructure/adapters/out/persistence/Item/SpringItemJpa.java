package com.mercadolibre.inventory.central.infrastructure.adapters.out.persistence.Item;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SpringItemJpa extends JpaRepository<ItemEntity, String> {
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("select i from ItemEntity i where i.sku = :sku")
  Optional<ItemEntity> findForUpdate(@Param("sku")  String sku);
}

