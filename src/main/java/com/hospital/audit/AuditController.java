package com.hospital.audit;

import com.hospital.common.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/audit")
@RequiredArgsConstructor
@Tag(name = "Audit Trail")
@SecurityRequirement(name = "bearerAuth")
@PreAuthorize("hasRole('ADMIN')")
public class AuditController {

    private final AuditService auditService;

    @GetMapping
    @Operation(summary = "Get all audit logs")
    public ResponseEntity<PageResponse<AuditResponse>> getAllLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        return ResponseEntity.ok(PageResponse.of(
                auditService.getAllAuditLogs(PageRequest.of(page, size, Sort.by("timestamp").descending())),
                "Audit logs retrieved"));
    }

    @GetMapping("/entity/{entityType}/{entityId}")
    @Operation(summary = "Get audit logs for a specific entity")
    public ResponseEntity<PageResponse<AuditResponse>> getEntityLogs(
            @PathVariable String entityType,
            @PathVariable String entityId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        return ResponseEntity.ok(PageResponse.of(
                auditService.getAuditLogsForEntity(entityType, entityId,
                        PageRequest.of(page, size, Sort.by("timestamp").descending())),
                "Audit logs retrieved"));
    }

    @GetMapping("/actor/{actor}")
    @Operation(summary = "Get audit logs by actor")
    public ResponseEntity<PageResponse<AuditResponse>> getActorLogs(
            @PathVariable String actor,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        return ResponseEntity.ok(PageResponse.of(
                auditService.getAuditLogsByActor(actor,
                        PageRequest.of(page, size, Sort.by("timestamp").descending())),
                "Audit logs retrieved"));
    }
}
