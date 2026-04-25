package com.hospital.medicalrecord;

import com.hospital.common.ApiResponse;
import com.hospital.common.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/medical-records")
@RequiredArgsConstructor
@Tag(name = "Medical Records")
@SecurityRequirement(name = "bearerAuth")
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    @Operation(summary = "Create medical record")
    public ResponseEntity<ApiResponse<MedicalRecordResponse>> createRecord(@Valid @RequestBody MedicalRecordRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Medical record created", medicalRecordService.createRecord(request)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','NURSE')")
    @Operation(summary = "Get medical record by ID")
    public ResponseEntity<ApiResponse<MedicalRecordResponse>> getRecord(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success("Record retrieved", medicalRecordService.getRecordById(id)));
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','NURSE')")
    @Operation(summary = "Get medical records by patient")
    public ResponseEntity<PageResponse<MedicalRecordResponse>> getByPatient(
            @PathVariable UUID patientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(PageResponse.of(
                medicalRecordService.getRecordsByPatient(patientId,
                        PageRequest.of(page, size, Sort.by("createdAt").descending())),
                "Records retrieved"));
    }

    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    @Operation(summary = "Get medical records by doctor")
    public ResponseEntity<PageResponse<MedicalRecordResponse>> getByDoctor(
            @PathVariable UUID doctorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(PageResponse.of(
                medicalRecordService.getRecordsByDoctor(doctorId,
                        PageRequest.of(page, size, Sort.by("createdAt").descending())),
                "Records retrieved"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    @Operation(summary = "Update medical record")
    public ResponseEntity<ApiResponse<MedicalRecordResponse>> updateRecord(
            @PathVariable UUID id, @Valid @RequestBody MedicalRecordRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Record updated", medicalRecordService.updateRecord(id, request)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete medical record")
    public ResponseEntity<ApiResponse<Void>> deleteRecord(@PathVariable UUID id) {
        medicalRecordService.deleteRecord(id);
        return ResponseEntity.ok(ApiResponse.success("Record deleted"));
    }
}
