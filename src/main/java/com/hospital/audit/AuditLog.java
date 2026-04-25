package com.hospital.audit;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "audit_logs", indexes = {
        @Index(name = "idx_audit_entity", columnList = "entity_type,entity_id"),
        @Index(name = "idx_audit_actor", columnList = "actor"),
        @Index(name = "idx_audit_timestamp", columnList = "timestamp")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 100)
    private String entityType;

    @Column(length = 36)
    private String entityId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AuditAction action;

    @Column(columnDefinition = "TEXT")
    private String changedFields;

    @Column(columnDefinition = "TEXT")
    private String oldValues;

    @Column(columnDefinition = "TEXT")
    private String newValues;

    @Column(nullable = false, length = 150)
    private String actor;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    @Column(length = 45)
    private String ipAddress;

    @Column(length = 200)
    private String userAgent;
}
