package com.mercadolibre.inventory.central.infrastructure.adapters.out.persistence.Reservation;

import com.mercadolibre.inventory.central.domain.model.ReservationState;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.*;
import java.util.Optional;

@Repository
public interface SpringReservationJpa extends JpaRepository<ReservationEntity, String> {
  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("select r from ReservationEntity r where r.reservationId = :id")
  Optional<ReservationEntity> findForUpdate(String id);

  Page<ReservationEntity> findBySku(String sku, Pageable pageable);
  Page<ReservationEntity> findByState(ReservationState state, Pageable pageable);
  Page<ReservationEntity> findBySkuAndState(String sku, ReservationState state, Pageable pageable);

}
