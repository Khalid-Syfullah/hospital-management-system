package com.hospital.billing;

import com.hospital.common.BaseEntity;
import com.hospital.patient.Patient;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "invoices", indexes = @Index(name = "idx_billing_status", columnList = "status"))
public class Invoice extends BaseEntity {
    public enum PaymentStatus { PENDING, PAID, PARTIAL, OVERDUE, CANCELLED, REFUNDED }
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "patient_id") private Patient patient;
    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true) private List<InvoiceItem> items = new ArrayList<>();
    @Column(nullable = false, precision = 12, scale = 2) private BigDecimal total = BigDecimal.ZERO;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private PaymentStatus status = PaymentStatus.PENDING;
    private String insuranceClaimNumber;
    @Column(unique = true) private String paymentIdempotencyKey;
    @Version private long version;
    public Patient getPatient() { return patient; } public void setPatient(Patient patient) { this.patient = patient; }
    public List<InvoiceItem> getItems() { return items; } public void setItems(List<InvoiceItem> items) { this.items = items; }
    public BigDecimal getTotal() { return total; } public void setTotal(BigDecimal total) { this.total = total; }
    public PaymentStatus getStatus() { return status; } public void setStatus(PaymentStatus status) { this.status = status; }
    public String getInsuranceClaimNumber() { return insuranceClaimNumber; } public void setInsuranceClaimNumber(String insuranceClaimNumber) { this.insuranceClaimNumber = insuranceClaimNumber; }
    public String getPaymentIdempotencyKey() { return paymentIdempotencyKey; } public void setPaymentIdempotencyKey(String paymentIdempotencyKey) { this.paymentIdempotencyKey = paymentIdempotencyKey; }
}
