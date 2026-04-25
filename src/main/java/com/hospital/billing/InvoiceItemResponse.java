package com.hospital.billing;

import java.math.BigDecimal;

public record InvoiceItemResponse(String description, BigDecimal unitPrice, int quantity, BigDecimal lineTotal) {
    static InvoiceItemResponse from(InvoiceItem i) { return new InvoiceItemResponse(i.getDescription(), i.getUnitPrice(), i.getQuantity(), i.getLineTotal()); }
}
