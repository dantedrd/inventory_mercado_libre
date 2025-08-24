package com.mercadolibre.inventory.central.infrastructure.adapters.out.persistence.Reservation;

import com.mercadolibre.inventory.central.domain.model.Reservation;
import com.mercadolibre.inventory.central.domain.model.ReservationPageable;
import com.mercadolibre.inventory.central.domain.model.ReservationState;
import com.mercadolibre.inventory.central.domain.ports.ReservationRepositoryPort;
import com.mercadolibre.inventory.shared.models.PageableRequest;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class JpaReservationRepository implements ReservationRepositoryPort {
  private final SpringReservationJpa jpa;

  public JpaReservationRepository(SpringReservationJpa jpa){
      this.jpa = jpa;
  }

  public Optional<Reservation> findForUpdate(String id){
      return jpa.findForUpdate(id)
                .map(ReservationTransformer::toDomain);
  }

  @Transactional
  public Reservation save(Reservation r){
      ReservationEntity entity = ReservationTransformer.toEntity(r);
      ReservationEntity saved = jpa.save(entity);
      return ReservationTransformer.toDomain(saved);
  }

    @Override
    public ReservationPageable listReservations(Optional<String> sku, Optional<ReservationState> state, PageableRequest pageableRequest) {
        Pageable pageable = PageRequest.of(pageableRequest.getPage(),pageableRequest.getSize(), Sort.by(Sort.Direction.DESC, pageableRequest.getSortBy()));
        Page<ReservationEntity> p;
        if (sku.isPresent() && state.isPresent()) {
            p = jpa.findBySkuAndState(sku.get(), state.get(), pageable);
        } else if (sku.isPresent()) {
            p = jpa.findBySku(sku.get(), pageable);
        } else if (state.isPresent()) {
            p = jpa.findByState(state.get(), pageable);
        } else {
            p = jpa.findAll(pageable);
        }

        List<Reservation> reservations=  p.stream()
                .map(ReservationTransformer::toDomain)
                .toList();

        return ReservationPageable
                .builder()
                .number(p.getNumber())
                .size(p.getTotalPages())
                .totalPages(p.getTotalPages())
                .reservations(reservations)
                .build();

    }


}
