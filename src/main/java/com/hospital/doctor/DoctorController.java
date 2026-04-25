package com.hospital.doctor;

import com.hospital.common.ApiResponse;
import com.hospital.common.PageResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/doctors")
@PreAuthorize("hasAnyRole('ADMIN','DOCTOR','NURSE','RECEPTIONIST')")
public class DoctorController {
    private final DoctorService service;
    public DoctorController(DoctorService service) { this.service = service; }
    @GetMapping PageResponse<DoctorResponse> list(Pageable pageable) { return PageResponse.of("Doctors retrieved", service.list(pageable)); }
    @GetMapping("/{id}") ApiResponse<DoctorResponse> get(@PathVariable UUID id) { return ApiResponse.ok("Doctor retrieved", service.get(id)); }
    @GetMapping("/{id}/availability") ApiResponse<String> availability(@PathVariable UUID id) { return ApiResponse.ok("Availability retrieved", service.availability(id)); }
    @PostMapping @PreAuthorize("hasRole('ADMIN')") ApiResponse<DoctorResponse> create(@Valid @RequestBody DoctorRequest request) { return ApiResponse.ok("Doctor created", service.create(request)); }
    @PutMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") ApiResponse<DoctorResponse> update(@PathVariable UUID id, @Valid @RequestBody DoctorRequest request) { return ApiResponse.ok("Doctor updated", service.update(id, request)); }
}
