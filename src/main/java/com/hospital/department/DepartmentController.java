package com.hospital.department;

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

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<DepartmentResponse>> createDepartment(@Valid @RequestBody DepartmentCreateRequest request) {
        Department department = departmentService.createDepartment(request);
        return ResponseEntity.ok(ApiResponse.success("Department created successfully", DepartmentResponse.from(department)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE', 'RECEPTIONIST')")
    public ResponseEntity<ApiResponse<DepartmentResponse>> getDepartment(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(DepartmentResponse.from(departmentService.getDepartmentById(id))));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE', 'RECEPTIONIST', 'PATIENT')")
    public ResponseEntity<ApiResponse<PageResponse<DepartmentResponse>>> getAllDepartments(
            @PageableDefault(size = 20) Pageable pageable) {
        Page<Department> departments = departmentService.getAllDepartments(pageable);
        return ResponseEntity.ok(ApiResponse.success(
                PageResponse.of(departments.getNumber(), departments.getSize(), departments.getTotalElements(),
                        departments.getContent().stream().map(DepartmentResponse::from).toList())
        ));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<DepartmentResponse>> updateDepartment(
            @PathVariable UUID id,
            @Valid @RequestBody DepartmentUpdateRequest request) {
        Department department = departmentService.updateDepartment(id, request);
        return ResponseEntity.ok(ApiResponse.success("Department updated successfully", DepartmentResponse.from(department)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteDepartment(@PathVariable UUID id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.ok(ApiResponse.success("Department deactivated successfully", null));
    }
}