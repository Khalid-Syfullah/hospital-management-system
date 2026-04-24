package com.hospital.doctor;

import com.hospital.common.ApiResponse;
import com.hospital.common.PageResponse;
import com.hospital.doctor.DoctorDtos.DoctorRequest;
import com.hospital.doctor.DoctorDtos.DoctorResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/doctors")
public class DoctorController {
    private final DoctorService service;

    public DoctorController(DoctorService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ApiResponse<DoctorResponse> create(@Valid @RequestBody DoctorRequest request) {
        return ApiResponse.success("Doctor created", service.create(request));
    }

    @GetMapping
    PageResponse<DoctorResponse> list(Pageable pageable) {
        return PageResponse.from("Doctors fetched", service.list(pageable));
    }

    @GetMapping("/{id}")
    ApiResponse<DoctorResponse> get(@PathVariable UUID id) {
        return ApiResponse.success("Doctor fetched", service.get(id));
    }
}
