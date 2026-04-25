package com.hospital.billing;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;

public record InvoiceItemRequest(@NotBlank String description, @DecimalMin("0.00") BigDecimal unitPrice, @Min(1) int quantity) {}
