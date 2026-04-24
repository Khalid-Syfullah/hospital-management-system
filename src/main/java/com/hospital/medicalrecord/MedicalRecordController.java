package com.hospital.medicalrecord;

import com.hospital.common.ApiResponse;
import com.hospital.common.PageResponse;
import com.hospital.medicalrecord.MedicalRecordDtos.MedicalRecordRequest;
import com.hospital.medicalrecord.MedicalRecordDtos.MedicalRecordResponse;
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
@RequestMapping("/api/v1/medical-records")
public class MedicalRecordController {
    private final MedicalRecordService service;

    public MedicalRecordController(MedicalRecordService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ApiResponse<MedicalRecordResponse> create(@Valid @RequestBody MedicalRecordRequest request) {
        return ApiResponse.success("Medical record created", service.create(request));
    }

    @GetMapping
    PageResponse<MedicalRecordResponse> list(Pageable pageable) {
        return PageResponse.from("Medical records fetched", service.list(pageable));
    }
}
