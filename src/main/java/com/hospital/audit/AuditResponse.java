package com.hospital.audit;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class AuditResponse {
    private UUID id;
    private String entityType;
    private String entityId;
    private AuditAction action;
    private String changedFields;
    private String oldValues;
    private String newValues;
    private String actor;
    private LocalDateTime timestamp;
    private String ipAddress;
}
