package com.hospital.department;

import com.hospital.common.ApiResponse;
import com.hospital.common.PageResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/departments")
public class DepartmentController {
    private final DepartmentService service;
    public DepartmentController(DepartmentService service) { this.service = service; }
    @GetMapping PageResponse<DepartmentResponse> list(Pageable pageable) { return PageResponse.of("Departments retrieved", service.list(pageable)); }
    @GetMapping("/{id}") ApiResponse<DepartmentResponse> get(@PathVariable UUID id) { return ApiResponse.ok("Department retrieved", service.get(id)); }
    @PostMapping @PreAuthorize("hasRole('ADMIN')") ApiResponse<DepartmentResponse> create(@Valid @RequestBody DepartmentRequest request) { return ApiResponse.ok("Department created", service.create(request)); }
    @PutMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") ApiResponse<DepartmentResponse> update(@PathVariable UUID id, @Valid @RequestBody DepartmentRequest request) { return ApiResponse.ok("Department updated", service.update(id, request)); }
}
