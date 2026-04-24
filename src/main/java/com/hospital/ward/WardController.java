package com.hospital.ward;

import com.hospital.common.ApiResponse;
import com.hospital.common.PageResponse;
import com.hospital.ward.WardDtos.AdmissionRequest;
import com.hospital.ward.WardDtos.BedRequest;
import com.hospital.ward.WardDtos.BedResponse;
import com.hospital.ward.WardDtos.WardRequest;
import com.hospital.ward.WardDtos.WardResponse;
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
@RequestMapping("/api/v1/wards")
public class WardController {
    private final WardService service;

    public WardController(WardService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ApiResponse<WardResponse> createWard(@Valid @RequestBody WardRequest request) {
        return ApiResponse.success("Ward created", service.createWard(request));
    }

    @PostMapping("/beds")
    @ResponseStatus(HttpStatus.CREATED)
    ApiResponse<BedResponse> createBed(@Valid @RequestBody BedRequest request) {
        return ApiResponse.success("Bed created", service.createBed(request));
    }

    @PatchMapping("/beds/{id}/admit")
    ApiResponse<BedResponse> admit(@PathVariable UUID id, @Valid @RequestBody AdmissionRequest request) {
        return ApiResponse.success("Patient admitted", service.admit(id, request));
    }

    @GetMapping("/beds")
    PageResponse<BedResponse> listBeds(Pageable pageable) {
        return PageResponse.from("Beds fetched", service.listBeds(pageable));
    }
}
