package com.hospital.billing;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class InvoiceCreateRequest {
    @NotNull private UUID patientId;
    private LocalDateTime dueDate;
    private String notes;
    private BigDecimal tax;
    private BigDecimal discount;
    private String idempotencyKey;
    private List<InvoiceItemRequest> items;
}