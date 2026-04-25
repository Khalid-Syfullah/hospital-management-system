package com.hospital.doctor;

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
@RequestMapping("/api/v1/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<DoctorResponse>> createDoctor(@Valid @RequestBody DoctorCreateRequest request) {
        Doctor doctor = doctorService.createDoctor(request);
        return ResponseEntity.ok(ApiResponse.success("Doctor registered successfully", DoctorResponse.from(doctor)));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE', 'RECEPTIONIST')")
    public ResponseEntity<ApiResponse<DoctorResponse>> getDoctor(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(DoctorResponse.from(doctorService.getDoctorById(id))));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE', 'RECEPTIONIST')")
    public ResponseEntity<ApiResponse<PageResponse<DoctorResponse>>> getAllDoctors(
            @PageableDefault(size = 20) Pageable pageable) {
        Page<Doctor> doctors = doctorService.getAllDoctors(pageable);
        return ResponseEntity.ok(ApiResponse.success(
                PageResponse.of(doctors.getNumber(), doctors.getSize(), doctors.getTotalElements(),
                        doctors.getContent().stream().map(DoctorResponse::from).toList())
        ));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE', 'RECEPTIONIST')")
    public ResponseEntity<ApiResponse<PageResponse<DoctorResponse>>> searchDoctors(
            @RequestParam String keyword,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<Doctor> doctors = doctorService.searchDoctors(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(
                PageResponse.of(doctors.getNumber(), doctors.getSize(), doctors.getTotalElements(),
                        doctors.getContent().stream().map(DoctorResponse::from).toList())
        ));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<ApiResponse<DoctorResponse>> updateDoctor(
            @PathVariable UUID id,
            @Valid @RequestBody DoctorUpdateRequest request) {
        Doctor doctor = doctorService.updateDoctor(id, request);
        return ResponseEntity.ok(ApiResponse.success("Doctor updated successfully", DoctorResponse.from(doctor)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteDoctor(@PathVariable UUID id) {
        doctorService.deleteDoctor(id);
        return ResponseEntity.ok(ApiResponse.success("Doctor deleted successfully", null));
    }
}