package com.mercadolibre.inventory.store.infrastructure.adapters.out.persistence.StoreOutbox;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "store_outbox")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreOutboxEntity {
    @Id
    private String id;
    private String routingKey;
    @Column(columnDefinition = "text") private String payload;
    private String status; // PENDING | SENT | ERROR
    private int attempts;
    private OffsetDateTime createdAt;
    private OffsetDateTime lastTriedAt;

    @PrePersist
    void pre() {
        if (id == null) id = UUID.randomUUID().toString();
        if (status == null) status = "PENDING";
        if (createdAt == null) createdAt = OffsetDateTime.now();
    }
}
