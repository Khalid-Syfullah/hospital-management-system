package com.hospital.pharmacy;

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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/pharmacy")
@RequiredArgsConstructor
@Tag(name = "Pharmacy & Inventory")
@SecurityRequirement(name = "bearerAuth")
public class PharmacyController {

    private final PharmacyService pharmacyService;

    @PostMapping("/medications")
    @PreAuthorize("hasAnyRole('ADMIN','PHARMACIST')")
    @Operation(summary = "Add medication to catalog")
    public ResponseEntity<ApiResponse<MedicationResponse>> createMedication(@Valid @RequestBody MedicationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Medication added", pharmacyService.createMedication(request)));
    }

    @GetMapping("/medications")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "List all medications")
    public ResponseEntity<PageResponse<MedicationResponse>> getAllMedications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(PageResponse.of(
                pharmacyService.getAllMedications(PageRequest.of(page, size, Sort.by("name"))),
                "Medications retrieved"));
    }

    @GetMapping("/medications/{id}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Get medication by ID")
    public ResponseEntity<ApiResponse<MedicationResponse>> getMedication(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success("Medication retrieved", pharmacyService.getMedicationById(id)));
    }

    @PutMapping("/medications/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','PHARMACIST')")
    @Operation(summary = "Update medication")
    public ResponseEntity<ApiResponse<MedicationResponse>> updateMedication(
            @PathVariable UUID id, @Valid @RequestBody MedicationRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Medication updated", pharmacyService.updateMedication(id, request)));
    }

    @PatchMapping("/medications/{id}/stock")
    @PreAuthorize("hasAnyRole('ADMIN','PHARMACIST')")
    @Operation(summary = "Adjust stock (positive=add, negative=remove)")
    public ResponseEntity<ApiResponse<MedicationResponse>> adjustStock(
            @PathVariable UUID id, @RequestParam int quantity) {
        return ResponseEntity.ok(ApiResponse.success("Stock adjusted", pharmacyService.adjustStock(id, quantity)));
    }

    @PostMapping("/dispense")
    @PreAuthorize("hasAnyRole('ADMIN','PHARMACIST')")
    @Operation(summary = "Dispense medication")
    public ResponseEntity<ApiResponse<Void>> dispense(
            @Valid @RequestBody DispenseRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        pharmacyService.dispenseMedication(request, userDetails.getUsername());
        return ResponseEntity.ok(ApiResponse.success("Medication dispensed"));
    }

    @GetMapping("/medications/low-stock")
    @PreAuthorize("hasAnyRole('ADMIN','PHARMACIST')")
    @Operation(summary = "Get low stock medications")
    public ResponseEntity<ApiResponse<List<MedicationResponse>>> getLowStock() {
        return ResponseEntity.ok(ApiResponse.success("Low stock medications retrieved",
                pharmacyService.getLowStockMedications()));
    }
}
