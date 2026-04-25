package com.hospital.appointment;

import com.hospital.common.ApiResponse;
import com.hospital.common.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<ApiResponse<AppointmentResponse>> createAppointment(
            @Valid @RequestBody AppointmentCreateRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Appointment created successfully",
                AppointmentResponse.from(appointmentService.createAppointment(request))));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE', 'RECEPTIONIST')")
    public ResponseEntity<ApiResponse<AppointmentResponse>> getAppointment(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(AppointmentResponse.from(appointmentService.getAppointmentById(id))));
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE', 'RECEPTIONIST', 'PATIENT')")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getAppointmentsByPatient(@PathVariable UUID patientId) {
        return ResponseEntity.ok(ApiResponse.success(
                appointmentService.getAppointmentsByPatient(patientId).stream()
                        .map(AppointmentResponse::from).toList()));
    }

    @GetMapping("/doctor/{doctorId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE', 'RECEPTIONIST')")
    public ResponseEntity<ApiResponse<List<AppointmentResponse>>> getAppointmentsByDoctor(@PathVariable UUID doctorId) {
        return ResponseEntity.ok(ApiResponse.success(
                appointmentService.getAppointmentsByDoctor(doctorId).stream()
                        .map(AppointmentResponse::from).toList()));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE', 'RECEPTIONIST')")
    public ResponseEntity<ApiResponse<PageResponse<AppointmentResponse>>> getAllAppointments(
            @PageableDefault(size = 20) Pageable pageable) {
        Page<Appointment> appointments = appointmentService.getAllAppointments(pageable);
        return ResponseEntity.ok(ApiResponse.success(
                PageResponse.of(appointments.getNumber(), appointments.getSize(), appointments.getTotalElements(),
                        appointments.getContent().stream().map(AppointmentResponse::from).toList())
        ));
    }

    @GetMapping("/range")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'NURSE', 'RECEPTIONIST')")
    public ResponseEntity<ApiResponse<PageResponse<AppointmentResponse>>> getAppointmentsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @PageableDefault(size = 20) Pageable pageable) {
        Page<Appointment> appointments = appointmentService.getAppointmentsByDateRange(start, end, pageable);
        return ResponseEntity.ok(ApiResponse.success(
                PageResponse.of(appointments.getNumber(), appointments.getSize(), appointments.getTotalElements(),
                        appointments.getContent().stream().map(AppointmentResponse::from).toList())
        ));
    }

    @PutMapping("/{id}/confirm")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<ApiResponse<AppointmentResponse>> confirmAppointment(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success("Appointment confirmed",
                AppointmentResponse.from(appointmentService.confirmAppointment(id))));
    }

    @PutMapping("/{id}/start")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<ApiResponse<AppointmentResponse>> startAppointment(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success("Appointment started",
                AppointmentResponse.from(appointmentService.startAppointment(id))));
    }

    @PutMapping("/{id}/complete")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<ApiResponse<AppointmentResponse>> completeAppointment(
            @PathVariable UUID id,
            @RequestParam(required = false) String notes) {
        return ResponseEntity.ok(ApiResponse.success("Appointment completed",
                AppointmentResponse.from(appointmentService.completeAppointment(id, notes))));
    }

    @PutMapping("/{id}/reschedule")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'PATIENT')")
    public ResponseEntity<ApiResponse<AppointmentResponse>> rescheduleAppointment(
            @PathVariable UUID id,
            @Valid @RequestBody AppointmentRescheduleRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Appointment rescheduled",
                AppointmentResponse.from(appointmentService.rescheduleAppointment(id, request))));
    }

    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'PATIENT')")
    public ResponseEntity<ApiResponse<AppointmentResponse>> cancelAppointment(
            @PathVariable UUID id,
            @RequestParam String reason) {
        return ResponseEntity.ok(ApiResponse.success("Appointment cancelled",
                AppointmentResponse.from(appointmentService.cancelAppointment(id, reason))));
    }
}