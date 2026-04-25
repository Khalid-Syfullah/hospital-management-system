package com.hospital.lab;

import com.hospital.common.ApiResponse;
import com.hospital.common.PageResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/lab-orders")
@PreAuthorize("hasAnyRole('ADMIN','DOCTOR','NURSE','LAB_TECHNICIAN')")
public class LabOrderController {
    private final LabOrderService service;
    public LabOrderController(LabOrderService service) { this.service = service; }
    @GetMapping PageResponse<LabOrderResponse> list(Pageable pageable) { return PageResponse.of("Lab orders retrieved", service.list(pageable)); }
    @GetMapping("/{id}") ApiResponse<LabOrderResponse> get(@PathVariable UUID id) { return ApiResponse.ok("Lab order retrieved", service.get(id)); }
    @PostMapping ApiResponse<LabOrderResponse> create(@Valid @RequestBody LabOrderRequest request) { return ApiResponse.ok("Lab order created", service.create(request)); }
    @PutMapping("/{id}") ApiResponse<LabOrderResponse> update(@PathVariable UUID id, @Valid @RequestBody LabOrderRequest request) { return ApiResponse.ok("Lab order updated", service.update(id, request)); }
    @DeleteMapping("/{id}") ApiResponse<LabOrderResponse> cancel(@PathVariable UUID id) { return ApiResponse.ok("Lab order cancelled", service.cancel(id)); }
}
