package com.hospital.billing;

import com.hospital.billing.BillingDtos.CreateInvoiceRequest;
import com.hospital.billing.BillingDtos.InvoiceResponse;
import com.hospital.billing.BillingDtos.PaymentRequest;
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
@RequestMapping("/api/v1/billing/invoices")
public class BillingController {
    private final BillingService service;

    public BillingController(BillingService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ApiResponse<InvoiceResponse> create(@Valid @RequestBody CreateInvoiceRequest request) {
        return ApiResponse.success("Invoice created", service.create(request));
    }

    @GetMapping
    PageResponse<InvoiceResponse> list(Pageable pageable) {
        return PageResponse.from("Invoices fetched", service.list(pageable));
    }

    @PatchMapping("/{id}/payment")
    ApiResponse<InvoiceResponse> payment(
            @PathVariable UUID id,
            @Valid @RequestBody PaymentRequest request,
            @RequestHeader(value = "Idempotency-Key", required = false) String idempotencyKey) {
        return ApiResponse.success("Payment recorded", service.recordPayment(id, request, idempotencyKey));
    }
}
