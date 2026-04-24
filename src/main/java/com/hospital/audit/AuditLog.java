package com.hospital.audit;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "audit_logs")
public class AuditLog {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String entityType;

    @Column(nullable = false)
    private String entityId;

    @Column(nullable = false)
    private String action;

    @Column(length = 4000)
    private String changedFields;

    private String actor;

    @Column(nullable = false, updatable = false)
    private Instant timestamp = Instant.now();

    protected AuditLog() {
    }

    public AuditLog(String entityType, String entityId, String action, String changedFields, String actor) {
        this.entityType = entityType;
        this.entityId = entityId;
        this.action = action;
        this.changedFields = changedFields;
        this.actor = actor;
    }

    public UUID getId() {
        return id;
    }

    public String getEntityType() {
        return entityType;
    }

    public String getEntityId() {
        return entityId;
    }

    public String getAction() {
        return action;
    }

    public String getChangedFields() {
        return changedFields;
    }

    public String getActor() {
        return actor;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
