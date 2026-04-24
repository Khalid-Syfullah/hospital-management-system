package com.hospital.audit;

import com.hospital.audit.AuditDtos.AuditLogResponse;
import com.hospital.common.PageResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/audit-logs")
public class AuditController {
    private final AuditLogRepository auditLogRepository;
    private final AuditMapper auditMapper;

    public AuditController(AuditLogRepository auditLogRepository, AuditMapper auditMapper) {
        this.auditLogRepository = auditLogRepository;
        this.auditMapper = auditMapper;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    PageResponse<AuditLogResponse> list(@RequestParam(required = false) String entityType, Pageable pageable) {
        var page = entityType == null
                ? auditLogRepository.findAll(pageable).map(auditMapper::toResponse)
                : auditLogRepository.findByEntityTypeIgnoreCase(entityType, pageable).map(auditMapper::toResponse);
        return PageResponse.from("Audit logs fetched", page);
    }
}
