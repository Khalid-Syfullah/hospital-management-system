package com.hospital.billing;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class InvoiceResponse {
    private UUID id;
    private String invoiceNumber;
    private UUID patientId;
    private String patientName;
    private String patientMrn;
    private LocalDate invoiceDate;
    private LocalDate dueDate;
    private PaymentStatus status;
    private BigDecimal subtotal;
    private BigDecimal taxAmount;
    private BigDecimal discountAmount;
    private BigDecimal totalAmount;
    private BigDecimal paidAmount;
    private String insuranceClaimNumber;
    private BigDecimal insuranceCoveredAmount;
    private String notes;
    private List<BillingItemResponse> items;
    private LocalDateTime createdAt;
}

@Data
class BillingItemResponse {
    private UUID id;
    private String description;
    private ItemType itemType;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal lineTotal;
}
