package com.hospital.lab;

import com.hospital.common.ApiResponse;
import com.hospital.common.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/lab")
@RequiredArgsConstructor
@Tag(name = "Lab Management")
@SecurityRequirement(name = "bearerAuth")
public class LabController {

    private final LabService labService;

    @PostMapping("/orders")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    @Operation(summary = "Create lab order")
    public ResponseEntity<ApiResponse<LabOrderResponse>> createOrder(@Valid @RequestBody LabOrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Lab order created", labService.createLabOrder(request)));
    }

    @GetMapping("/orders/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','NURSE','LAB_TECHNICIAN')")
    @Operation(summary = "Get lab order by ID")
    public ResponseEntity<ApiResponse<LabOrderResponse>> getOrder(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success("Lab order retrieved", labService.getLabOrderById(id)));
    }

    @GetMapping("/orders/patient/{patientId}")
    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','NURSE','LAB_TECHNICIAN')")
    @Operation(summary = "Get lab orders by patient")
    public ResponseEntity<PageResponse<LabOrderResponse>> getByPatient(
            @PathVariable UUID patientId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(PageResponse.of(
                labService.getByPatient(patientId, PageRequest.of(page, size, Sort.by("createdAt").descending())),
                "Lab orders retrieved"));
    }

    @GetMapping("/orders/status/{status}")
    @PreAuthorize("hasAnyRole('ADMIN','LAB_TECHNICIAN')")
    @Operation(summary = "Get lab orders by status")
    public ResponseEntity<PageResponse<LabOrderResponse>> getByStatus(
            @PathVariable LabStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(PageResponse.of(
                labService.getByStatus(status, PageRequest.of(page, size, Sort.by("createdAt").descending())),
                "Lab orders retrieved"));
    }

    @PatchMapping("/orders/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN','LAB_TECHNICIAN')")
    @Operation(summary = "Update lab order status")
    public ResponseEntity<ApiResponse<LabOrderResponse>> updateStatus(
            @PathVariable UUID id, @RequestParam LabStatus status) {
        return ResponseEntity.ok(ApiResponse.success("Status updated", labService.updateStatus(id, status)));
    }

    @PostMapping("/orders/{id}/results")
    @PreAuthorize("hasAnyRole('ADMIN','LAB_TECHNICIAN')")
    @Operation(summary = "Add lab result")
    public ResponseEntity<ApiResponse<LabOrderResponse>> addResult(
            @PathVariable UUID id, @RequestBody LabResultRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Result added", labService.addResult(id, request)));
    }
}
