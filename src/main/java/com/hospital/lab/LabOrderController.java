package com.hospital.lab;

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

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/lab-orders")
@RequiredArgsConstructor
public class LabOrderController {

    private final LabOrderService labOrderService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<ApiResponse<LabOrderResponse>> createLabOrder(@Valid @RequestBody LabOrderCreateRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Lab order created", LabOrderResponse.from(labOrderService.createLabOrder(request))));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'LAB_TECHNICIAN')")
    public ResponseEntity<ApiResponse<LabOrderResponse>> getLabOrder(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(LabOrderResponse.from(labOrderService.getLabOrderById(id))));
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'LAB_TECHNICIAN', 'PATIENT')")
    public ResponseEntity<ApiResponse<List<LabOrderResponse>>> getLabOrdersByPatient(@PathVariable UUID patientId) {
        return ResponseEntity.ok(ApiResponse.success(labOrderService.getLabOrdersByPatient(patientId).stream().map(LabOrderResponse::from).toList()));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'LAB_TECHNICIAN')")
    public ResponseEntity<ApiResponse<PageResponse<LabOrderResponse>>> getAllLabOrders(@PageableDefault(size = 20) Pageable pageable) {
        Page<LabOrder> orders = labOrderService.getAllLabOrders(pageable);
        return ResponseEntity.ok(ApiResponse.success(PageResponse.of(orders.getNumber(), orders.getSize(), orders.getTotalElements(), orders.getContent().stream().map(LabOrderResponse::from).toList())));
    }

    @PutMapping("/{id}/start")
    @PreAuthorize("hasAnyRole('ADMIN', 'LAB_TECHNICIAN')")
    public ResponseEntity<ApiResponse<LabOrderResponse>> startLabOrder(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success("Lab order started", LabOrderResponse.from(labOrderService.startLabOrder(id))));
    }

    @PutMapping("/{id}/complete")
    @PreAuthorize("hasAnyRole('ADMIN', 'LAB_TECHNICIAN')")
    public ResponseEntity<ApiResponse<LabOrderResponse>> completeLabOrder(@PathVariable UUID id, @RequestParam String results) {
        return ResponseEntity.ok(ApiResponse.success("Lab order completed", LabOrderResponse.from(labOrderService.completeLabOrder(id, results))));
    }

    @PutMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR')")
    public ResponseEntity<ApiResponse<LabOrderResponse>> cancelLabOrder(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success("Lab order cancelled", LabOrderResponse.from(labOrderService.cancelLabOrder(id))));
    }
}