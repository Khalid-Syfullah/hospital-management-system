package com.hospital.medicalrecord;

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
@RequestMapping("/api/v1/medical-records")
@RequiredArgsConstructor
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<ApiResponse<MedicalRecordResponse>> createMedicalRecord(
            @Valid @RequestBody MedicalRecordCreateRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Medical record created",
                MedicalRecordResponse.from(medicalRecordService.createMedicalRecord(request))));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<ApiResponse<MedicalRecordResponse>> getMedicalRecord(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(MedicalRecordResponse.from(medicalRecordService.getMedicalRecordById(id))));
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE', 'PATIENT')")
    public ResponseEntity<ApiResponse<List<MedicalRecordResponse>>> getMedicalRecordsByPatient(@PathVariable UUID patientId) {
        return ResponseEntity.ok(ApiResponse.success(
                medicalRecordService.getMedicalRecordsByPatient(patientId).stream()
                        .map(MedicalRecordResponse::from).toList()));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<ApiResponse<PageResponse<MedicalRecordResponse>>> getAllMedicalRecords(
            @PageableDefault(size = 20) Pageable pageable) {
        Page<MedicalRecord> records = medicalRecordService.getAllMedicalRecords(pageable);
        return ResponseEntity.ok(ApiResponse.success(
                PageResponse.of(records.getNumber(), records.getSize(), records.getTotalElements(),
                        records.getContent().stream().map(MedicalRecordResponse::from).toList())
        ));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<ApiResponse<MedicalRecordResponse>> updateMedicalRecord(
            @PathVariable UUID id,
            @Valid @RequestBody MedicalRecordUpdateRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Medical record updated",
                MedicalRecordResponse.from(medicalRecordService.updateMedicalRecord(id, request))));
    }
}