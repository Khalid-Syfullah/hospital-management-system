package com.hospital.billing;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class InvoiceRequest {

    @NotNull
    private UUID patientId;

    @NotNull
    private LocalDate invoiceDate;

    private LocalDate dueDate;

    private BigDecimal taxAmount;

    private BigDecimal discountAmount;

    private String notes;

    private String idempotencyKey;

    @NotEmpty
    @Valid
    private List<BillingItemRequest> items;
}
