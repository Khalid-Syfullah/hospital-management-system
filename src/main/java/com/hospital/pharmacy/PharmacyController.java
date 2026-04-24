package com.hospital.pharmacy;

import com.hospital.common.ApiResponse;
import com.hospital.common.PageResponse;
import com.hospital.pharmacy.PharmacyDtos.DispenseRequest;
import com.hospital.pharmacy.PharmacyDtos.MedicationRequest;
import com.hospital.pharmacy.PharmacyDtos.MedicationResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/pharmacy/medications")
public class PharmacyController {
    private final PharmacyService service;

    public PharmacyController(PharmacyService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ApiResponse<MedicationResponse> create(@Valid @RequestBody MedicationRequest request) {
        return ApiResponse.success("Medication created", service.create(request));
    }

    @GetMapping
    PageResponse<MedicationResponse> list(Pageable pageable) {
        return PageResponse.from("Medication catalog fetched", service.list(pageable));
    }

    @PatchMapping("/{id}/dispense")
    ApiResponse<MedicationResponse> dispense(@PathVariable UUID id, @Valid @RequestBody DispenseRequest request) {
        return ApiResponse.success("Medication dispensed", service.dispense(id, request));
    }
}
