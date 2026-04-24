package com.hospital.audit;

import java.time.Instant;
import java.util.UUID;

public final class AuditDtos {
    private AuditDtos() {
    }

    public record AuditLogResponse(
            UUID id, String entityType, String entityId, String action, String changedFields, String actor, Instant timestamp) {
    }
}
