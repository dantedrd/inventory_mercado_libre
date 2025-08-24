package com.mercadolibre.inventory.central.infrastructure.adapters.out.persistence.Reservation;

import com.mercadolibre.inventory.central.domain.model.Reservation;
import com.mercadolibre.inventory.central.domain.model.ReservationPageable;
import com.mercadolibre.inventory.central.domain.model.ReservationState;
import com.mercadolibre.inventory.shared.models.PageableRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JpaReservationRepositoryTest {
    @Mock
    private SpringReservationJpa jpa;
    private JpaReservationRepository repository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        repository = new JpaReservationRepository(jpa);
    }

    @Test
    void testFindForUpdateFound() {
        ReservationEntity entity = new ReservationEntity();

        Reservation reservation = new Reservation();
        when(jpa.findForUpdate("id1")).thenReturn(Optional.of(entity));
        try (var transformerMock = Mockito.mockStatic(ReservationTransformer.class)) {
            transformerMock.when(() -> ReservationTransformer.toDomain(entity)).thenReturn(reservation);
            Optional<Reservation> result = repository.findForUpdate("id1");
            assertTrue(result.isPresent());
            assertEquals(reservation, result.get());
        }
    }

    @Test
    void testFindForUpdateNotFound() {
        when(jpa.findForUpdate("id2")).thenReturn(Optional.empty());
        Optional<Reservation> result = repository.findForUpdate("id2");
        assertFalse(result.isPresent());
    }

    @Test
    void testSave() {
        Reservation reservation = new Reservation();
        ReservationEntity entity = new ReservationEntity();
        ReservationEntity savedEntity = new ReservationEntity();
        when(jpa.save(any())).thenReturn(savedEntity);
        try (var transformerMock = Mockito.mockStatic(ReservationTransformer.class)) {
            transformerMock.when(() -> ReservationTransformer.toEntity(reservation)).thenReturn(entity);
            transformerMock.when(() -> ReservationTransformer.toDomain(savedEntity)).thenReturn(reservation);
            Reservation result = repository.save(reservation);
            assertEquals(reservation, result);
        }
    }

    @Test
    void testListReservationsWithSkuAndState() {
        PageableRequest pageableRequest = PageableRequest.builder().page(0).size(2).sortBy("createdAt").build();
        Pageable pageable = PageRequest.of(0, 2, org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "createdAt"));
        ReservationEntity entity = new ReservationEntity();
        Reservation reservation = new Reservation();
        Page<ReservationEntity> page = new PageImpl<>(List.of(entity), pageable, 1);
        when(jpa.findBySkuAndState("sku1", ReservationState.RESERVED, pageable)).thenReturn(page);
        try (var transformerMock = Mockito.mockStatic(ReservationTransformer.class)) {
            transformerMock.when(() -> ReservationTransformer.toDomain(entity)).thenReturn(reservation);
            ReservationPageable result = repository.listReservations(Optional.of("sku1"), Optional.of(ReservationState.RESERVED), pageableRequest);
            assertEquals(1, result.getReservations().size());
            assertEquals(reservation, result.getReservations().get(0));
        }
    }

    @Test
    void testListReservationsWithSkuOnly() {
        PageableRequest pageableRequest = PageableRequest.builder().page(0).size(2).sortBy("createdAt").build();
        Pageable pageable = PageRequest.of(0, 2, org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "createdAt"));
        ReservationEntity entity = new ReservationEntity();
        Reservation reservation = new Reservation();
        Page<ReservationEntity> page = new PageImpl<>(List.of(entity), pageable, 1);
        when(jpa.findBySku("sku2", pageable)).thenReturn(page);
        try (var transformerMock = Mockito.mockStatic(ReservationTransformer.class)) {
            transformerMock.when(() -> ReservationTransformer.toDomain(entity)).thenReturn(reservation);
            ReservationPageable result = repository.listReservations(Optional.of("sku2"), Optional.empty(), pageableRequest);
            assertEquals(1, result.getReservations().size());
            assertEquals(reservation, result.getReservations().get(0));
        }
    }

    @Test
    void testListReservationsWithStateOnly() {
        PageableRequest pageableRequest = PageableRequest.builder().page(0).size(2).sortBy("createdAt").build();
        Pageable pageable = PageRequest.of(0, 2, org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "createdAt"));
        ReservationEntity entity = new ReservationEntity();
        Reservation reservation = new Reservation();
        Page<ReservationEntity> page = new PageImpl<>(List.of(entity), pageable, 1);
        when(jpa.findByState(ReservationState.COMMITTED, pageable)).thenReturn(page);
        try (var transformerMock = Mockito.mockStatic(ReservationTransformer.class)) {
            transformerMock.when(() -> ReservationTransformer.toDomain(entity)).thenReturn(reservation);
            ReservationPageable result = repository.listReservations(Optional.empty(), Optional.of(ReservationState.COMMITTED), pageableRequest);
            assertEquals(1, result.getReservations().size());
            assertEquals(reservation, result.getReservations().get(0));
        }
    }

    @Test
    void testListReservationsWithoutFilters() {
        PageableRequest pageableRequest = PageableRequest.builder().page(0).size(2).sortBy("createdAt").build();
        Pageable pageable = PageRequest.of(0, 2, org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "createdAt"));
        ReservationEntity entity = new ReservationEntity();
        Reservation reservation = new Reservation();
        Page<ReservationEntity> page = new PageImpl<>(List.of(entity), pageable, 1);
        when(jpa.findAll(pageable)).thenReturn(page);
        try (var transformerMock = Mockito.mockStatic(ReservationTransformer.class)) {
            transformerMock.when(() -> ReservationTransformer.toDomain(entity)).thenReturn(reservation);
            ReservationPageable result = repository.listReservations(Optional.empty(), Optional.empty(), pageableRequest);
            assertEquals(1, result.getReservations().size());
            assertEquals(reservation, result.getReservations().get(0));
        }
    }
}

