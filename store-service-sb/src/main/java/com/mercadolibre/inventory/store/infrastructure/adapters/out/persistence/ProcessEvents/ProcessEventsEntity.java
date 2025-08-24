package com.mercadolibre.inventory.store.infrastructure.adapters.out.persistence.ProcessEvents;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "processed_event")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessEventsEntity {
    @Id
    private String eventId;
}

