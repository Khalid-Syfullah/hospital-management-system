package com.hospital.prescription;

import com.hospital.common.ApiResponse;
import com.hospital.common.PageResponse;
import com.hospital.prescription.PrescriptionDtos.PrescriptionRequest;
import com.hospital.prescription.PrescriptionDtos.PrescriptionResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/prescriptions")
public class PrescriptionController {
    private final PrescriptionService service;

    public PrescriptionController(PrescriptionService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ApiResponse<PrescriptionResponse> create(@Valid @RequestBody PrescriptionRequest request) {
        return ApiResponse.success("Prescription created", service.create(request));
    }

    @GetMapping
    PageResponse<PrescriptionResponse> list(Pageable pageable) {
        return PageResponse.from("Prescriptions fetched", service.list(pageable));
    }
}
