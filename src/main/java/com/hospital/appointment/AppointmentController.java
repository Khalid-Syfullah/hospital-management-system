package com.hospital.appointment;

import com.hospital.common.ApiResponse;
import com.hospital.common.PageResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/appointments")
@PreAuthorize("hasAnyRole('ADMIN','DOCTOR','NURSE','RECEPTIONIST','PATIENT')")
public class AppointmentController {
    private final AppointmentService service;
    public AppointmentController(AppointmentService service) { this.service = service; }
    @GetMapping PageResponse<AppointmentResponse> list(Pageable pageable) { return PageResponse.of("Appointments retrieved", service.list(pageable)); }
    @GetMapping("/{id}") ApiResponse<AppointmentResponse> get(@PathVariable UUID id) { return ApiResponse.ok("Appointment retrieved", service.get(id)); }
    @PostMapping ApiResponse<AppointmentResponse> book(@Valid @RequestBody AppointmentRequest request, @RequestHeader("Idempotency-Key") String idempotencyKey) { return ApiResponse.ok("Appointment booked", service.book(request, idempotencyKey)); }
    @PutMapping("/{id}") ApiResponse<AppointmentResponse> reschedule(@PathVariable UUID id, @Valid @RequestBody AppointmentRequest request) { return ApiResponse.ok("Appointment rescheduled", service.reschedule(id, request)); }
    @DeleteMapping("/{id}") ApiResponse<AppointmentResponse> cancel(@PathVariable UUID id) { return ApiResponse.ok("Appointment cancelled", service.cancel(id)); }
}
