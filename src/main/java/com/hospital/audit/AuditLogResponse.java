package com.hospital.audit;

import java.time.Instant;
import java.util.UUID;

public record AuditLogResponse(UUID id, String entityType, String entityId, String action, String changedFields,
                               String actor, Instant timestamp) {
    static AuditLogResponse from(AuditLog log) {
        return new AuditLogResponse(log.getId(), log.getEntityType(), log.getEntityId(), log.getAction(),
                log.getChangedFields(), log.getActor(), log.getTimestamp());
    }
}
