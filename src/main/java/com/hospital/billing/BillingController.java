package com.hospital.billing;

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

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/billing")
@RequiredArgsConstructor
public class BillingController {

    private final BillingService billingService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<ApiResponse<InvoiceResponse>> createInvoice(@Valid @RequestBody InvoiceCreateRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Invoice created", InvoiceResponse.from(billingService.createInvoice(request))));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'PATIENT')")
    public ResponseEntity<ApiResponse<InvoiceResponse>> getInvoice(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(InvoiceResponse.from(billingService.getInvoiceById(id))));
    }

    @GetMapping("/number/{invoiceNumber}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'PATIENT')")
    public ResponseEntity<ApiResponse<InvoiceResponse>> getInvoiceByNumber(@PathVariable String invoiceNumber) {
        return ResponseEntity.ok(ApiResponse.success(InvoiceResponse.from(billingService.getInvoiceByNumber(invoiceNumber))));
    }

    @GetMapping("/patient/{patientId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'PATIENT')")
    public ResponseEntity<ApiResponse<List<InvoiceResponse>>> getInvoicesByPatient(@PathVariable UUID patientId) {
        return ResponseEntity.ok(ApiResponse.success(billingService.getInvoicesByPatient(patientId).stream().map(InvoiceResponse::from).toList()));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST')")
    public ResponseEntity<ApiResponse<PageResponse<InvoiceResponse>>> getAllInvoices(@PageableDefault(size = 20) Pageable pageable) {
        Page<Invoice> invoices = billingService.getAllInvoices(pageable);
        return ResponseEntity.ok(ApiResponse.success(PageResponse.of(invoices.getNumber(), invoices.getSize(), invoices.getTotalElements(), invoices.getContent().stream().map(InvoiceResponse::from).toList())));
    }

    @PostMapping("/{id}/payment")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPTIONIST', 'PATIENT')")
    public ResponseEntity<ApiResponse<InvoiceResponse>> makePayment(@PathVariable UUID id, @RequestParam BigDecimal amount) {
        return ResponseEntity.ok(ApiResponse.success("Payment successful", InvoiceResponse.from(billingService.makePayment(id, amount))));
    }

    @PostMapping("/{id}/items")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<InvoiceResponse>> addInvoiceItem(@PathVariable UUID id, @Valid @RequestBody InvoiceItemRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Item added", InvoiceResponse.from(billingService.addInvoiceItem(id, request))));
    }
}