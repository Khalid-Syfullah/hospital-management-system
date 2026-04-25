package com.hospital.ward;

import com.hospital.common.ApiResponse;
import com.hospital.common.PageResponse;
import com.hospital.patient.PatientService;
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
@RequestMapping("/api/v1/wards")
@RequiredArgsConstructor
public class WardController {

    private final WardService wardService;
    private final PatientService patientService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<WardResponse>> createWard(@Valid @RequestBody WardCreateRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Ward created", WardResponse.from(wardService.createWard(request))));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'NURSE')")
    public ResponseEntity<ApiResponse<WardResponse>> getWard(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(WardResponse.from(wardService.getWardById(id))));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'NURSE', 'RECEPTIONIST')")
    public ResponseEntity<ApiResponse<PageResponse<WardResponse>>> getAllWards(@PageableDefault(size = 20) Pageable pageable) {
        Page<Ward> wards = wardService.getAllWards(pageable);
        return ResponseEntity.ok(ApiResponse.success(PageResponse.of(wards.getNumber(), wards.getSize(), wards.getTotalElements(), wards.getContent().stream().map(WardResponse::from).toList())));
    }

    @GetMapping("/{wardId}/beds")
    @PreAuthorize("hasAnyRole('ADMIN', 'NURSE', 'RECEPTIONIST')")
    public ResponseEntity<ApiResponse<List<BedResponse>>> getBedsByWard(@PathVariable UUID wardId) {
        return ResponseEntity.ok(ApiResponse.success(wardService.getBedsByWard(wardId).stream().map(BedResponse::from).toList()));
    }

    @PostMapping("/beds/{bedId}/admit")
    @PreAuthorize("hasAnyRole('ADMIN', 'NURSE', 'RECEPTIONIST')")
    public ResponseEntity<ApiResponse<BedResponse>> admitPatient(@PathVariable UUID bedId, @RequestParam UUID patientId) {
        return ResponseEntity.ok(ApiResponse.success("Patient admitted", BedResponse.from(wardService.admitPatient(bedId, patientId))));
    }

    @PostMapping("/beds/{bedId}/discharge")
    @PreAuthorize("hasAnyRole('ADMIN', 'NURSE', 'RECEPTIONIST')")
    public ResponseEntity<ApiResponse<BedResponse>> dischargePatient(@PathVariable UUID bedId) {
        return ResponseEntity.ok(ApiResponse.success("Patient discharged", BedResponse.from(wardService.dischargePatient(bedId))));
    }
}