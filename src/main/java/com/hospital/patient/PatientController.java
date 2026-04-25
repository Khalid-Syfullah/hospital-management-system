package com.hospital.patient;

import com.hospital.common.ApiResponse;
import com.hospital.common.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<ApiResponse<PatientResponse>> createPatient(@Valid @RequestBody PatientCreateRequest request) {
        Patient patient = patientService.createPatient(request);
        return ResponseEntity.ok(ApiResponse.success("Patient registered successfully", PatientResponse.from(patient)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE', 'RECEPTIONIST')")
    public ResponseEntity<ApiResponse<PatientResponse>> getPatient(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(PatientResponse.from(patientService.getPatientById(id))));
    }

    @GetMapping("/mrn/{mrn}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE', 'RECEPTIONIST')")
    public ResponseEntity<ApiResponse<PatientResponse>> getPatientByMrn(@PathVariable String mrn) {
        return ResponseEntity.ok(ApiResponse.success(PatientResponse.from(patientService.getPatientByMrn(mrn))));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE', 'RECEPTIONIST')")
    public ResponseEntity<ApiResponse<PageResponse<PatientResponse>>> getAllPatients(
            @PageableDefault(size = 20) Pageable pageable) {
        Page<Patient> patients = patientService.getAllPatients(pageable);
        return ResponseEntity.ok(ApiResponse.success(
                PageResponse.of(patients.getNumber(), patients.getSize(), patients.getTotalElements(),
                        patients.getContent().stream().map(PatientResponse::from).toList())
        ));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE', 'RECEPTIONIST')")
    public ResponseEntity<ApiResponse<PageResponse<PatientResponse>>> searchPatients(
            @RequestParam String keyword,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<Patient> patients = patientService.searchPatients(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(
                PageResponse.of(patients.getNumber(), patients.getSize(), patients.getTotalElements(),
                        patients.getContent().stream().map(PatientResponse::from).toList())
        ));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PATIENT')")
    public ResponseEntity<ApiResponse<PatientResponse>> updatePatient(
            @PathVariable UUID id,
            @Valid @RequestBody PatientUpdateRequest request) {
        Patient patient = patientService.updatePatient(id, request);
        return ResponseEntity.ok(ApiResponse.success("Patient updated successfully", PatientResponse.from(patient)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deletePatient(@PathVariable UUID id) {
        patientService.deletePatient(id);
        return ResponseEntity.ok(ApiResponse.success("Patient deleted successfully", null));
    }
}