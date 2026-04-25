package com.hospital.audit;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditRepository auditRepository;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void log(String entityType, String entityId, AuditAction action,
                    String changedFields, String oldValues, String newValues) {
        String actor = resolveActor();
        AuditLog auditLog = AuditLog.builder()
                .entityType(entityType)
                .entityId(entityId)
                .action(action)
                .changedFields(changedFields)
                .oldValues(oldValues)
                .newValues(newValues)
                .actor(actor)
                .timestamp(LocalDateTime.now())
                .build();
        auditRepository.save(auditLog);
    }

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void log(String entityType, String entityId, AuditAction action) {
        log(entityType, entityId, action, null, null, null);
    }

    @Transactional(readOnly = true)
    public Page<AuditResponse> getAuditLogsForEntity(String entityType, String entityId, Pageable pageable) {
        return auditRepository.findByEntityTypeAndEntityId(entityType, entityId, pageable)
                .map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<AuditResponse> getAuditLogsByActor(String actor, Pageable pageable) {
        return auditRepository.findByActor(actor, pageable).map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public Page<AuditResponse> getAllAuditLogs(Pageable pageable) {
        return auditRepository.findAll(pageable).map(this::toResponse);
    }

    private String resolveActor() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) {
            return auth.getName();
        }
        return "system";
    }

    private AuditResponse toResponse(AuditLog log) {
        AuditResponse resp = new AuditResponse();
        resp.setId(log.getId());
        resp.setEntityType(log.getEntityType());
        resp.setEntityId(log.getEntityId());
        resp.setAction(log.getAction());
        resp.setChangedFields(log.getChangedFields());
        resp.setOldValues(log.getOldValues());
        resp.setNewValues(log.getNewValues());
        resp.setActor(log.getActor());
        resp.setTimestamp(log.getTimestamp());
        resp.setIpAddress(log.getIpAddress());
        return resp;
    }
}
