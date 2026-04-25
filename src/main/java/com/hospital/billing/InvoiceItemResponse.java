package com.hospital.billing;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
public class InvoiceItemResponse {
    private UUID id;
    private String description;
    private String itemType;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal amount;

    public static InvoiceItemResponse from(InvoiceItem item) {
        return new InvoiceItemResponse(
                item.getId(),
                item.getDescription(),
                item.getItemType(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.getAmount()
        );
    }
}