package com.hospital.doctor;

import com.hospital.common.ApiResponse;
import com.hospital.common.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<DoctorResponse>> createDoctor(@RequestBody DoctorRequest request) {
        log.info("Creating new doctor: {} {}", request.getFirstName(), request.getLastName());
        DoctorResponse response = doctorService.createDoctor(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Doctor created successfully", response));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PATIENT', 'NURSE')")
    public ResponseEntity<ApiResponse<DoctorResponse>> getDoctor(@PathVariable UUID id) {
        log.info("Fetching doctor: {}", id);
        DoctorResponse response = doctorService.getDoctor(id);
        return ResponseEntity.ok(ApiResponse.success("Doctor retrieved successfully", response));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<DoctorResponse>> updateDoctor(@PathVariable UUID id, @RequestBody DoctorRequest request) {
        log.info("Updating doctor: {}", id);
        DoctorResponse response = doctorService.updateDoctor(id, request);
        return ResponseEntity.ok(ApiResponse.success("Doctor updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteDoctor(@PathVariable UUID id) {
        log.info("Deleting doctor: {}", id);
        doctorService.deleteDoctor(id);
        return ResponseEntity.ok(ApiResponse.success("Doctor deleted successfully", ""));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PATIENT', 'NURSE')")
    public ResponseEntity<PageResponse<DoctorResponse>> getAllDoctors(Pageable pageable) {
        log.info("Fetching all doctors");
        Page<DoctorResponse> page = doctorService.getAllDoctors(pageable);
        return ResponseEntity.ok(PageResponse.of("Doctors retrieved successfully", page.getContent(),
                pageable.getPageNumber(), pageable.getPageSize(), page.getTotalElements(), page.getTotalPages()));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'PATIENT', 'NURSE')")
    public ResponseEntity<PageResponse<DoctorResponse>> searchDoctors(@RequestParam String keyword, Pageable pageable) {
        log.info("Searching doctors with keyword: {}", keyword);
        Page<DoctorResponse> page = doctorService.searchDoctors(keyword, pageable);
        return ResponseEntity.ok(PageResponse.of("Doctors found", page.getContent(),
                pageable.getPageNumber(), pageable.getPageSize(), page.getTotalElements(), page.getTotalPages()));
    }

    @GetMapping("/department/{departmentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PATIENT', 'NURSE')")
    public ResponseEntity<PageResponse<DoctorResponse>> getDoctorsByDepartment(@PathVariable UUID departmentId, Pageable pageable) {
        log.info("Fetching doctors by department: {}", departmentId);
        Page<DoctorResponse> page = doctorService.getDoctorsByDepartment(departmentId, pageable);
        return ResponseEntity.ok(PageResponse.of("Doctors retrieved successfully", page.getContent(),
                pageable.getPageNumber(), pageable.getPageSize(), page.getTotalElements(), page.getTotalPages()));
    }
}
