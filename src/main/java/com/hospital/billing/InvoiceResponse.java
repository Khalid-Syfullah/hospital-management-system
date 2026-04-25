package com.hospital.billing;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record InvoiceResponse(UUID id, UUID patientId, BigDecimal total, Invoice.PaymentStatus status,
                              String insuranceClaimNumber, List<InvoiceItemResponse> items) {
    static InvoiceResponse from(Invoice i) { return new InvoiceResponse(i.getId(), i.getPatient().getId(), i.getTotal(), i.getStatus(), i.getInsuranceClaimNumber(), i.getItems().stream().map(InvoiceItemResponse::from).toList()); }
}
