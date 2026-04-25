package com.hospital.audit;

import com.hospital.user.User;
import com.hospital.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logAction(String entityType, UUID entityId, AuditLog.AuditAction action, String changedFields, UUID actorId, String actorIp) {
        AuditLog auditLog = new AuditLog();
        auditLog.setEntityType(entityType);
        auditLog.setEntityId(entityId);
        auditLog.setAction(action);
        auditLog.setChangedFields(changedFields);
        auditLog.setActorIp(actorIp);

        if (actorId != null) {
            userRepository.findById(actorId).ifPresent(user -> {
                auditLog.setActorId(user.getId());
                auditLog.setActorEmail(user.getEmail());
            });
        }

        auditLogRepository.save(auditLog);
        log.info("Audit log created: {} {} on {}:{}", action, entityType, entityId);
    }

    public Page<AuditLog> getAuditLogs(Pageable pageable) {
        return auditLogRepository.findAll(pageable);
    }

    public Page<AuditLog> getAuditLogsByEntity(String entityType, UUID entityId, Pageable pageable) {
        return auditLogRepository.findByEntityTypeAndEntityId(entityType, entityId, pageable);
    }
}