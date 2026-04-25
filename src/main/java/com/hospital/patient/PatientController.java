package com.hospital.patient;

import com.hospital.common.ApiResponse;
import com.hospital.common.PageResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/patients")
@PreAuthorize("hasAnyRole('ADMIN','DOCTOR','NURSE','RECEPTIONIST')")
public class PatientController {
    private final PatientService service;
    public PatientController(PatientService service) { this.service = service; }
    @GetMapping PageResponse<PatientResponse> list(Pageable pageable) { return PageResponse.of("Patients retrieved", service.list(pageable)); }
    @GetMapping("/{id}") ApiResponse<PatientResponse> get(@PathVariable UUID id) { return ApiResponse.ok("Patient retrieved", service.get(id)); }
    @PostMapping ApiResponse<PatientResponse> create(@Valid @RequestBody PatientRequest request) { return ApiResponse.ok("Patient created", service.create(request)); }
    @PutMapping("/{id}") ApiResponse<PatientResponse> update(@PathVariable UUID id, @Valid @RequestBody PatientRequest request) { return ApiResponse.ok("Patient updated", service.update(id, request)); }
    @DeleteMapping("/{id}") ApiResponse<Void> delete(@PathVariable UUID id) { service.delete(id); return ApiResponse.ok("Patient deleted", null); }
}
