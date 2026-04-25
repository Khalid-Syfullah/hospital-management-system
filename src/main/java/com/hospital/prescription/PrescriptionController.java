package com.hospital.prescription;

import com.hospital.common.ApiResponse;
import com.hospital.common.PageResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/prescriptions")
@PreAuthorize("hasAnyRole('ADMIN','DOCTOR','NURSE','PHARMACIST')")
public class PrescriptionController {
    private final PrescriptionService service;
    public PrescriptionController(PrescriptionService service) { this.service = service; }
    @GetMapping PageResponse<PrescriptionResponse> list(Pageable pageable) { return PageResponse.of("Prescriptions retrieved", service.list(pageable)); }
    @GetMapping("/{id}") ApiResponse<PrescriptionResponse> get(@PathVariable UUID id) { return ApiResponse.ok("Prescription retrieved", service.get(id)); }
    @PostMapping ApiResponse<PrescriptionResponse> create(@Valid @RequestBody PrescriptionRequest request) { return ApiResponse.ok("Prescription created", service.create(request)); }
    @PutMapping("/{id}") ApiResponse<PrescriptionResponse> update(@PathVariable UUID id, @Valid @RequestBody PrescriptionRequest request) { return ApiResponse.ok("Prescription updated", service.update(id, request)); }
    @DeleteMapping("/{id}") ApiResponse<PrescriptionResponse> cancel(@PathVariable UUID id) { return ApiResponse.ok("Prescription cancelled", service.cancel(id)); }
}
