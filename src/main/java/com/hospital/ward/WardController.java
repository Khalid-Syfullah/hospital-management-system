package com.hospital.ward;

import com.hospital.common.ApiResponse;
import com.hospital.common.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/wards")
@RequiredArgsConstructor
@Tag(name = "Ward & Bed Management")
@SecurityRequirement(name = "bearerAuth")
public class WardController {

    private final WardService wardService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create ward")
    public ResponseEntity<ApiResponse<WardResponse>> createWard(@Valid @RequestBody WardRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Ward created", wardService.createWard(request)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get ward by ID")
    public ResponseEntity<ApiResponse<WardResponse>> getWard(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success("Ward retrieved", wardService.getWardById(id)));
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "List all wards")
    public ResponseEntity<PageResponse<WardResponse>> getAllWards(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(PageResponse.of(wardService.getAllWards(PageRequest.of(page, size)), "Wards retrieved"));
    }

    @PostMapping("/admissions")
    @PreAuthorize("hasAnyRole('ADMIN','NURSE','RECEPTIONIST')")
    @Operation(summary = "Admit patient to bed")
    public ResponseEntity<ApiResponse<AdmissionResponse>> admitPatient(@Valid @RequestBody AdmissionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Patient admitted", wardService.admitPatient(request)));
    }

    @PatchMapping("/admissions/{admissionId}/discharge")
    @PreAuthorize("hasAnyRole('ADMIN','NURSE','DOCTOR')")
    @Operation(summary = "Discharge patient")
    public ResponseEntity<ApiResponse<AdmissionResponse>> dischargePatient(
            @PathVariable UUID admissionId,
            @RequestParam(required = false) String dischargeNotes) {
        return ResponseEntity.ok(ApiResponse.success("Patient discharged",
                wardService.dischargePatient(admissionId, dischargeNotes)));
    }

    @GetMapping("/admissions/patient/{patientId}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','NURSE')")
    @Operation(summary = "Get admission history for patient")
    public ResponseEntity<PageResponse<AdmissionResponse>> getAdmissionsByPatient(
            @PathVariable UUID patientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(PageResponse.of(
                wardService.getAdmissionsByPatient(patientId, PageRequest.of(page, size)),
                "Admissions retrieved"));
    }
}
