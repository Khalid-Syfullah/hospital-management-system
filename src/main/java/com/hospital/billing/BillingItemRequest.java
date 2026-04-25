package com.hospital.billing;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BillingItemRequest {

    @NotBlank
    private String description;

    @NotNull
    private ItemType itemType;

    @NotNull
    @Min(1)
    private Integer quantity;

    @NotNull
    @Positive
    private BigDecimal unitPrice;
}
