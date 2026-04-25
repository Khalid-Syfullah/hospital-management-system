package com.hospital.audit;

import java.time.Instant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditService {
    private final AuditLogRepository repository;

    public AuditService(AuditLogRepository repository) {
        this.repository = repository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void record(String entityType, String entityId, String action, String changedFields) {
        AuditLog log = new AuditLog();
        log.setEntityType(entityType);
        log.setEntityId(entityId);
        log.setAction(action);
        log.setChangedFields(changedFields);
        log.setActor(actor());
        log.setTimestamp(Instant.now());
        repository.save(log);
    }

    @Transactional(readOnly = true)
    public Page<AuditLogResponse> list(Pageable pageable) {
        return repository.findAll(pageable).map(AuditLogResponse::from);
    }

    private String actor() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        return auth == null || !auth.isAuthenticated() ? "system" : auth.getName();
    }
}
