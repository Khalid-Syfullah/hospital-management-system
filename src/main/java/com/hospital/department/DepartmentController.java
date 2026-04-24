package com.hospital.department;

import com.hospital.common.ApiResponse;
import com.hospital.common.PageResponse;
import com.hospital.department.DepartmentDtos.DepartmentRequest;
import com.hospital.department.DepartmentDtos.DepartmentResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/departments")
public class DepartmentController {
    private final DepartmentService service;

    public DepartmentController(DepartmentService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ApiResponse<DepartmentResponse> create(@Valid @RequestBody DepartmentRequest request) {
        return ApiResponse.success("Department created", service.create(request));
    }

    @GetMapping
    PageResponse<DepartmentResponse> list(Pageable pageable) {
        return PageResponse.from("Departments fetched", service.list(pageable));
    }

    @PutMapping("/{id}")
    ApiResponse<DepartmentResponse> update(@PathVariable UUID id, @Valid @RequestBody DepartmentRequest request) {
        return ApiResponse.success("Department updated", service.update(id, request));
    }
}
