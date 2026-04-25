package com.hospital.appointment;

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
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    @PreAuthorize("hasAnyRole('PATIENT', 'RECEPTIONIST')")
    public ResponseEntity<ApiResponse<AppointmentResponse>> bookAppointment(@RequestBody AppointmentRequest request) {
        log.info("Booking new appointment");
        AppointmentResponse response = appointmentService.bookAppointment(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Appointment booked successfully", response));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'PATIENT', 'NURSE')")
    public ResponseEntity<ApiResponse<AppointmentResponse>> getAppointment(@PathVariable UUID id) {
        log.info("Fetching appointment: {}", id);
        AppointmentResponse response = appointmentService.getAppointment(id);
        return ResponseEntity.ok(ApiResponse.success("Appointment retrieved successfully", response));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('PATIENT', 'RECEPTIONIST')")
    public ResponseEntity<ApiResponse<AppointmentResponse>> updateAppointment(@PathVariable UUID id, @RequestBody AppointmentRequest request) {
        log.info("Updating appointment: {}", id);
        AppointmentResponse response = appointmentService.updateAppointment(id, request);
        return ResponseEntity.ok(ApiResponse.success("Appointment updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('PATIENT', 'RECEPTIONIST', 'ADMIN')")
    public ResponseEntity<ApiResponse<String>> cancelAppointment(@PathVariable UUID id) {
        log.info("Cancelling appointment: {}", id);
        appointmentService.cancelAppointment(id);
        return ResponseEntity.ok(ApiResponse.success("Appointment cancelled successfully", ""));
    }

    @PostMapping("/{id}/confirm")
    @PreAuthorize("hasAnyRole('RECEPTIONIST', 'DOCTOR')")
    public ResponseEntity<ApiResponse<AppointmentResponse>> confirmAppointment(@PathVariable UUID id) {
        log.info("Confirming appointment: {}", id);
        AppointmentResponse response = appointmentService.confirmAppointment(id);
        return ResponseEntity.ok(ApiResponse.success("Appointment confirmed successfully", response));
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'PATIENT', 'NURSE')")
    public ResponseEntity<PageResponse<AppointmentResponse>> getPatientAppointments(@PathVariable UUID patientId, Pageable pageable) {
        log.info("Fetching appointments for patient: {}", patientId);
        Page<AppointmentResponse> page = appointmentService.getPatientAppointments(patientId, pageable);
        return ResponseEntity.ok(PageResponse.of("Patient appointments retrieved successfully", page.getContent(),
                pageable.getPageNumber(), pageable.getPageSize(), page.getTotalElements(), page.getTotalPages()));
    }

    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<PageResponse<AppointmentResponse>> getDoctorAppointments(@PathVariable UUID doctorId, Pageable pageable) {
        log.info("Fetching appointments for doctor: {}", doctorId);
        Page<AppointmentResponse> page = appointmentService.getDoctorAppointments(doctorId, pageable);
        return ResponseEntity.ok(PageResponse.of("Doctor appointments retrieved successfully", page.getContent(),
                pageable.getPageNumber(), pageable.getPageSize(), page.getTotalElements(), page.getTotalPages()));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE')")
    public ResponseEntity<PageResponse<AppointmentResponse>> getAppointmentsByStatus(
            @PathVariable Appointment.AppointmentStatus status, Pageable pageable) {
        log.info("Fetching appointments with status: {}", status);
        Page<AppointmentResponse> page = appointmentService.getAppointmentsByStatus(status, pageable);
        return ResponseEntity.ok(PageResponse.of("Appointments retrieved successfully", page.getContent(),
                pageable.getPageNumber(), pageable.getPageSize(), page.getTotalElements(), page.getTotalPages()));
    }
}
