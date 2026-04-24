package com.hospital.billing;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public final class BillingDtos {
    private BillingDtos() {
    }

    public record BillingItemRequest(@NotBlank String description, @Min(1) int quantity, @DecimalMin("0.00") BigDecimal unitPrice) {
    }

    public record CreateInvoiceRequest(@NotNull UUID patientId, String insuranceClaimNumber, @Valid @NotEmpty List<BillingItemRequest> items) {
    }

    public record PaymentRequest(@NotNull PaymentStatus status) {
    }

    public record BillingItemResponse(UUID id, String description, int quantity, BigDecimal unitPrice, BigDecimal lineTotal) {
    }

    public record InvoiceResponse(
            UUID id, UUID patientId, BigDecimal total, PaymentStatus status, String insuranceClaimNumber,
            List<BillingItemResponse> items, long version) {
    }
}
