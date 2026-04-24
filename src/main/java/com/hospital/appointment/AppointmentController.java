package com.hospital.appointment;

import com.hospital.appointment.AppointmentDtos.AppointmentResponse;
import com.hospital.appointment.AppointmentDtos.BookAppointmentRequest;
import com.hospital.appointment.AppointmentDtos.RescheduleAppointmentRequest;
import com.hospital.common.ApiResponse;
import com.hospital.common.PageResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/appointments")
public class AppointmentController {
    private final AppointmentService service;

    public AppointmentController(AppointmentService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ApiResponse<AppointmentResponse> book(
            @Valid @RequestBody BookAppointmentRequest request,
            @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey) {
        return ApiResponse.success("Appointment booked", service.book(request, idempotencyKey));
    }

    @GetMapping
    PageResponse<AppointmentResponse> list(Pageable pageable) {
        return PageResponse.from("Appointments fetched", service.list(pageable));
    }

    @PatchMapping("/{id}/reschedule")
    ApiResponse<AppointmentResponse> reschedule(@PathVariable UUID id, @Valid @RequestBody RescheduleAppointmentRequest request) {
        return ApiResponse.success("Appointment rescheduled", service.reschedule(id, request));
    }

    @PatchMapping("/{id}/cancel")
    ApiResponse<AppointmentResponse> cancel(@PathVariable UUID id) {
        return ApiResponse.success("Appointment cancelled", service.cancel(id));
    }
}
