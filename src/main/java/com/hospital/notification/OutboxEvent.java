package com.hospital.notification;

import com.hospital.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "outbox_events")
public class OutboxEvent extends BaseEntity {
    @Column(nullable = false)
    private String eventType;
    @Column(nullable = false, length = 4000)
    private String payload;
    private Instant processedAt;

    protected OutboxEvent() {
    }

    public OutboxEvent(String eventType, String payload) {
        this.eventType = eventType;
        this.payload = payload;
    }

    public String getEventType() {
        return eventType;
    }

    public String getPayload() {
        return payload;
    }

    public Instant getProcessedAt() {
        return processedAt;
    }

    public void markProcessed() {
        processedAt = Instant.now();
    }
}
