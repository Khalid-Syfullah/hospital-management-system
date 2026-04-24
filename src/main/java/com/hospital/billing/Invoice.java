package com.hospital.billing;

import com.hospital.common.BaseEntity;
import com.hospital.patient.Patient;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "invoices")
public class Invoice extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_id")
    private Patient patient;
    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BillingItem> items = new ArrayList<>();
    @Column(nullable = false)
    private BigDecimal total = BigDecimal.ZERO;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status = PaymentStatus.PENDING;
    @Column(unique = true)
    private String paymentIdempotencyKey;
    private String insuranceClaimNumber;
    @Version
    private long version;

    protected Invoice() {
    }

    public Invoice(Patient patient, String insuranceClaimNumber) {
        this.patient = patient;
        this.insuranceClaimNumber = insuranceClaimNumber;
    }

    public Patient getPatient() {
        return patient;
    }

    public List<BillingItem> getItems() {
        return items;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public String getPaymentIdempotencyKey() {
        return paymentIdempotencyKey;
    }

    public String getInsuranceClaimNumber() {
        return insuranceClaimNumber;
    }

    public long getVersion() {
        return version;
    }

    public void replaceItems(List<BillingItem> newItems) {
        items.clear();
        newItems.forEach(item -> {
            item.setInvoice(this);
            items.add(item);
        });
        recalculateTotal();
    }

    public void markPayment(PaymentStatus status, String idempotencyKey) {
        this.status = status;
        this.paymentIdempotencyKey = idempotencyKey;
    }

    public void recalculateTotal() {
        total = items.stream().map(BillingItem::lineTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
