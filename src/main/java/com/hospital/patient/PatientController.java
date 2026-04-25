package com.hospital.patient;

import com.hospital.common.ApiResponse;
import com.hospital.common.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<ApiResponse<PatientResponse>> createPatient(@RequestBody PatientRequest request) {
        log.info("Creating new patient");
        PatientResponse response = patientService.createPatient(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Patient created successfully", response));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE', 'PATIENT')")
    public ResponseEntity<ApiResponse<PatientResponse>> getPatient(@PathVariable UUID id) {
        log.info("Fetching patient: {}", id);
        PatientResponse response = patientService.getPatient(id);
        return ResponseEntity.ok(ApiResponse.success("Patient retrieved successfully", response));
    }

    @GetMapping("/mrn/{mrn}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<ApiResponse<PatientResponse>> getPatientByMrn(@PathVariable String mrn) {
        log.info("Fetching patient by MRN: {}", mrn);
        PatientResponse response = patientService.getPatientByMrn(mrn);
        return ResponseEntity.ok(ApiResponse.success("Patient retrieved successfully", response));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<ApiResponse<PatientResponse>> updatePatient(@PathVariable UUID id, @RequestBody PatientRequest request) {
        log.info("Updating patient: {}", id);
        PatientResponse response = patientService.updatePatient(id, request);
        return ResponseEntity.ok(ApiResponse.success("Patient updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> deletePatient(@PathVariable UUID id) {
        log.info("Deleting patient: {}", id);
        patientService.deletePatient(id);
        return ResponseEntity.ok(ApiResponse.success("Patient deleted successfully", ""));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<PageResponse<PatientResponse>> getAllPatients(Pageable pageable) {
        log.info("Fetching all patients");
        Page<PatientResponse> page = patientService.getAllPatients(pageable);
        return ResponseEntity.ok(PageResponse.of("Patients retrieved successfully", page.getContent(),
                pageable.getPageNumber(), pageable.getPageSize(), page.getTotalElements(), page.getTotalPages()));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<PageResponse<PatientResponse>> searchPatients(@RequestParam String keyword, Pageable pageable) {
        log.info("Searching patients with keyword: {}", keyword);
        Page<PatientResponse> page = patientService.searchPatients(keyword, pageable);
        return ResponseEntity.ok(PageResponse.of("Patients found", page.getContent(),
                pageable.getPageNumber(), pageable.getPageSize(), page.getTotalElements(), page.getTotalPages()));
    }
}
