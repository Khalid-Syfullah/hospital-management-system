package com.hospital.audit;

import com.hospital.audit.AuditDtos.AuditLogResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuditMapper {
    AuditLogResponse toResponse(AuditLog auditLog);
}
