package com.hospital.billing;

import com.hospital.common.ApiResponse;
import com.hospital.common.PageResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/billing/invoices")
@PreAuthorize("hasAnyRole('ADMIN','RECEPTIONIST')")
public class BillingController {
    private final BillingService service;
    public BillingController(BillingService service) { this.service = service; }
    @GetMapping PageResponse<InvoiceResponse> list(Pageable pageable) { return PageResponse.of("Invoices retrieved", service.list(pageable)); }
    @GetMapping("/{id}") ApiResponse<InvoiceResponse> get(@PathVariable UUID id) { return ApiResponse.ok("Invoice retrieved", service.get(id)); }
    @PostMapping ApiResponse<InvoiceResponse> create(@Valid @RequestBody InvoiceRequest request) { return ApiResponse.ok("Invoice created", service.create(request)); }
    @PostMapping("/{id}/payments") ApiResponse<InvoiceResponse> pay(@PathVariable UUID id, @RequestHeader("Idempotency-Key") String key) { return ApiResponse.ok("Payment accepted", service.pay(id, key)); }
    @DeleteMapping("/{id}") ApiResponse<InvoiceResponse> cancel(@PathVariable UUID id) { return ApiResponse.ok("Invoice cancelled", service.cancel(id)); }
}
