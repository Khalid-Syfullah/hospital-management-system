package com.hospital.billing;

import com.hospital.common.BaseEntity;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "invoice_items")
public class InvoiceItem extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "invoice_id") private Invoice invoice;
    @Column(nullable = false) private String description;
    @Column(nullable = false, precision = 12, scale = 2) private BigDecimal unitPrice;
    @Column(nullable = false) private int quantity;
    @Column(nullable = false, precision = 12, scale = 2) private BigDecimal lineTotal;
    public Invoice getInvoice() { return invoice; } public void setInvoice(Invoice invoice) { this.invoice = invoice; }
    public String getDescription() { return description; } public void setDescription(String description) { this.description = description; }
    public BigDecimal getUnitPrice() { return unitPrice; } public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public int getQuantity() { return quantity; } public void setQuantity(int quantity) { this.quantity = quantity; }
    public BigDecimal getLineTotal() { return lineTotal; } public void setLineTotal(BigDecimal lineTotal) { this.lineTotal = lineTotal; }
}
