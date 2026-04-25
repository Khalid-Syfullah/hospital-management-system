package com.hospital.prescription;

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

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/prescriptions")
@RequiredArgsConstructor
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<ApiResponse<PrescriptionResponse>> createPrescription(@Valid @RequestBody PrescriptionCreateRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Prescription created", PrescriptionResponse.from(prescriptionService.createPrescription(request))));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PHARMACIST', 'PATIENT')")
    public ResponseEntity<ApiResponse<PrescriptionResponse>> getPrescription(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(PrescriptionResponse.from(prescriptionService.getPrescriptionById(id))));
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PHARMACIST', 'PATIENT')")
    public ResponseEntity<ApiResponse<List<PrescriptionResponse>>> getPrescriptionsByPatient(@PathVariable UUID patientId) {
        return ResponseEntity.ok(ApiResponse.success(prescriptionService.getPrescriptionsByPatient(patientId).stream().map(PrescriptionResponse::from).toList()));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PHARMACIST')")
    public ResponseEntity<ApiResponse<PageResponse<PrescriptionResponse>>> getAllPrescriptions(@PageableDefault(size = 20) Pageable pageable) {
        Page<Prescription> prescriptions = prescriptionService.getAllPrescriptions(pageable);
        return ResponseEntity.ok(ApiResponse.success(PageResponse.of(prescriptions.getNumber(), prescriptions.getSize(), prescriptions.getTotalElements(), prescriptions.getContent().stream().map(PrescriptionResponse::from).toList())));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<ApiResponse<PrescriptionResponse>> updatePrescription(@PathVariable UUID id, @Valid @RequestBody PrescriptionUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Prescription updated", PrescriptionResponse.from(prescriptionService.updatePrescription(id, request))));
    }

    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<ApiResponse<Void>> cancelPrescription(@PathVariable UUID id) {
        prescriptionService.cancelPrescription(id);
        return ResponseEntity.ok(ApiResponse.success("Prescription cancelled", null));
    }
}