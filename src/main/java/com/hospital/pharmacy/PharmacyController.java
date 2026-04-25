package com.hospital.pharmacy;

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

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/pharmacy")
@RequiredArgsConstructor
public class PharmacyController {

    private final PharmacyService pharmacyService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<MedicationResponse>> createMedication(@Valid @RequestBody MedicationCreateRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Medication created", MedicationResponse.from(pharmacyService.createMedication(request))));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACIST', 'DOCTOR')")
    public ResponseEntity<ApiResponse<MedicationResponse>> getMedication(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(MedicationResponse.from(pharmacyService.getMedicationById(id))));
    }

    @GetMapping("/low-stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACIST')")
    public ResponseEntity<ApiResponse<List<MedicationResponse>>> getLowStock() {
        return ResponseEntity.ok(ApiResponse.success(pharmacyService.getLowStockMedications().stream().map(MedicationResponse::from).toList()));
    }

    @GetMapping("/expiring")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<List<MedicationResponse>>> getExpiringSoon() {
        return ResponseEntity.ok(ApiResponse.success(pharmacyService.getExpiringSoon(LocalDate.now().plusMonths(3)).stream().map(MedicationResponse::from).toList()));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACIST', 'DOCTOR')")
    public ResponseEntity<ApiResponse<PageResponse<MedicationResponse>>> getAllMedications(@PageableDefault(size = 20) Pageable pageable) {
        Page<Medication> medications = pharmacyService.getAllMedications(pageable);
        return ResponseEntity.ok(ApiResponse.success(PageResponse.of(medications.getNumber(), medications.getSize(), medications.getTotalElements(), medications.getContent().stream().map(MedicationResponse::from).toList())));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACIST', 'DOCTOR')")
    public ResponseEntity<ApiResponse<PageResponse<MedicationResponse>>> searchMedications(@RequestParam String keyword, @PageableDefault(size = 20) Pageable pageable) {
        Page<Medication> medications = pharmacyService.searchMedications(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(PageResponse.of(medications.getNumber(), medications.getSize(), medications.getTotalElements(), medications.getContent().stream().map(MedicationResponse::from).toList())));
    }

    @PutMapping("/{id}/stock")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<MedicationResponse>> updateStock(@PathVariable UUID id, @RequestParam Integer quantity) {
        return ResponseEntity.ok(ApiResponse.success("Stock updated", MedicationResponse.from(pharmacyService.updateStock(id, quantity))));
    }

    @PostMapping("/{id}/dispense")
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACIST')")
    public ResponseEntity<ApiResponse<Void>> dispense(@PathVariable UUID id, @RequestParam Integer quantity) {
        pharmacyService.dispense(id, quantity);
        return ResponseEntity.ok(ApiResponse.success("Medication dispensed", null));
    }

    @PostMapping("/{id}/restock")
    @PreAuthorize("hasAnyRole('ADMIN', 'PHARMACIST')")
    public ResponseEntity<ApiResponse<MedicationResponse>> restock(@PathVariable UUID id, @RequestParam Integer quantity) {
        pharmacyService.restock(id, quantity);
        return ResponseEntity.ok(ApiResponse.success("Medication restocked", MedicationResponse.from(pharmacyService.getMedicationById(id))));
    }
}