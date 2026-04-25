package com.hospital.notification;

import com.hospital.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "notification_outbox")
public class NotificationOutbox extends BaseEntity {
    @Column(nullable = false) private String eventType;
    @Column(nullable = false) private String subject;
    @Column(nullable = false, length = 4000) private String payload;
    @Column(nullable = false) private boolean processed;
    private Instant processedAt;
    public String getEventType() { return eventType; } public void setEventType(String eventType) { this.eventType = eventType; }
    public String getSubject() { return subject; } public void setSubject(String subject) { this.subject = subject; }
    public String getPayload() { return payload; } public void setPayload(String payload) { this.payload = payload; }
    public boolean isProcessed() { return processed; } public void setProcessed(boolean processed) { this.processed = processed; }
    public Instant getProcessedAt() { return processedAt; } public void setProcessedAt(Instant processedAt) { this.processedAt = processedAt; }
}
