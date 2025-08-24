package com.mercadolibre.inventory.central.infrastructure.adapters.out.persistence;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "outbox")
public class OutboxEntity {
  @Id @GeneratedValue(strategy = GenerationType.UUID)
  private String id;
  private String routingKey;
  @Column(columnDefinition = "text")
  private String payload;
  private String status; // PENDING | SENT
  private OffsetDateTime createdAt;

  public String getId() { return id; }
  public void setId(String id) { this.id = id; }
  public String getRoutingKey() { return routingKey; }
  public void setRoutingKey(String routingKey) { this.routingKey = routingKey; }
  public String getPayload() { return payload; }
  public void setPayload(String payload) { this.payload = payload; }
  public String getStatus() { return status; }
  public void setStatus(String status) { this.status = status; }
  public OffsetDateTime getCreatedAt() { return createdAt; }
  public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
}
