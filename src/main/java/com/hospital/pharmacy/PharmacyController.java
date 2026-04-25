package com.hospital.pharmacy;

import com.hospital.common.ApiResponse;
import com.hospital.common.PageResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pharmacy/medications")
@PreAuthorize("hasAnyRole('ADMIN','PHARMACIST','DOCTOR','NURSE')")
public class PharmacyController {
    private final PharmacyService service;
    public PharmacyController(PharmacyService service) { this.service = service; }
    @GetMapping PageResponse<MedicationResponse> list(Pageable pageable) { return PageResponse.of("Medications retrieved", service.list(pageable)); }
    @GetMapping("/{id}") ApiResponse<MedicationResponse> get(@PathVariable UUID id) { return ApiResponse.ok("Medication retrieved", service.get(id)); }
    @PostMapping @PreAuthorize("hasAnyRole('ADMIN','PHARMACIST')") ApiResponse<MedicationResponse> create(@Valid @RequestBody MedicationRequest request) { return ApiResponse.ok("Medication created", service.create(request)); }
    @PutMapping("/{id}") @PreAuthorize("hasAnyRole('ADMIN','PHARMACIST')") ApiResponse<MedicationResponse> update(@PathVariable UUID id, @Valid @RequestBody MedicationRequest request) { return ApiResponse.ok("Medication updated", service.update(id, request)); }
    @PostMapping("/{id}/dispense") @PreAuthorize("hasRole('PHARMACIST')") ApiResponse<MedicationResponse> dispense(@PathVariable UUID id, @Valid @RequestBody DispenseRequest request) { return ApiResponse.ok("Medication dispensed", service.dispense(id, request)); }
    @DeleteMapping("/{id}") @PreAuthorize("hasAnyRole('ADMIN','PHARMACIST')") ApiResponse<Void> delete(@PathVariable UUID id) { service.delete(id); return ApiResponse.ok("Medication deleted", null); }
}
