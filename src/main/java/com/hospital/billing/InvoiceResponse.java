package com.hospital.billing;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class InvoiceResponse {
    private UUID id;
    private UUID patientId;
    private String patientName;
    private String invoiceNumber;
    private LocalDateTime invoiceDate;
    private String status;
    private BigDecimal subtotal;
    private BigDecimal tax;
    private BigDecimal discount;
    private BigDecimal total;
    private BigDecimal paidAmount;
    private LocalDateTime dueDate;
    private List<InvoiceItemResponse> items;
    private String notes;

    public static InvoiceResponse from(Invoice i) {
        return new InvoiceResponse(
                i.getId(),
                i.getPatient().getId(),
                i.getPatient().getUser().getFullName(),
                i.getInvoiceNumber(),
                i.getInvoiceDate(),
                i.getStatus().name(),
                i.getSubtotal(),
                i.getTax(),
                i.getDiscount(),
                i.getTotal(),
                i.getPaidAmount(),
                i.getDueDate(),
                i.getItems().stream().map(InvoiceItemResponse::from).toList(),
                i.getNotes()
        );
    }
}