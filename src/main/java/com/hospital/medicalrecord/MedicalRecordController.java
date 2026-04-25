package com.hospital.medicalrecord;

import com.hospital.common.ApiResponse;
import com.hospital.common.PageResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/medical-records")
@PreAuthorize("hasAnyRole('ADMIN','DOCTOR','NURSE')")
public class MedicalRecordController {
    private final MedicalRecordService service;
    public MedicalRecordController(MedicalRecordService service) { this.service = service; }
    @GetMapping PageResponse<MedicalRecordResponse> list(Pageable pageable) { return PageResponse.of("Medical records retrieved", service.list(pageable)); }
    @GetMapping("/{id}") ApiResponse<MedicalRecordResponse> get(@PathVariable UUID id) { return ApiResponse.ok("Medical record retrieved", service.get(id)); }
    @PostMapping ApiResponse<MedicalRecordResponse> create(@Valid @RequestBody MedicalRecordRequest request) { return ApiResponse.ok("Medical record created", service.create(request)); }
    @PutMapping("/{id}") ApiResponse<MedicalRecordResponse> update(@PathVariable UUID id, @Valid @RequestBody MedicalRecordRequest request) { return ApiResponse.ok("Medical record updated", service.update(id, request)); }
    @DeleteMapping("/{id}") ApiResponse<Void> delete(@PathVariable UUID id) { service.delete(id); return ApiResponse.ok("Medical record deleted", null); }
}
