package com.hospital.lab;

import com.hospital.common.ApiResponse;
import com.hospital.common.PageResponse;
import com.hospital.lab.LabDtos.LabOrderRequest;
import com.hospital.lab.LabDtos.LabOrderResponse;
import com.hospital.lab.LabDtos.LabResultRequest;
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
@RequestMapping("/api/v1/lab-orders")
public class LabController {
    private final LabService service;

    public LabController(LabService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ApiResponse<LabOrderResponse> create(@Valid @RequestBody LabOrderRequest request) {
        return ApiResponse.success("Lab order created", service.create(request));
    }

    @PatchMapping("/{id}/result")
    ApiResponse<LabOrderResponse> complete(@PathVariable UUID id, @Valid @RequestBody LabResultRequest request) {
        return ApiResponse.success("Lab result saved", service.complete(id, request));
    }

    @GetMapping
    PageResponse<LabOrderResponse> list(Pageable pageable) {
        return PageResponse.from("Lab orders fetched", service.list(pageable));
    }
}
