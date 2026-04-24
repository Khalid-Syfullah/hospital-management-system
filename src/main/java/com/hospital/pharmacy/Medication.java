package com.hospital.pharmacy;

import com.hospital.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.LocalDate;

@Entity
@Table(name = "medications")
public class Medication extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String name;
    private String form;
    @Column(nullable = false)
    private int stockQuantity;
    @Column(nullable = false)
    private int lowStockThreshold;
    private LocalDate expiryDate;
    @Version
    private long version;

    protected Medication() {
    }

    public Medication(String name, String form, int stockQuantity, int lowStockThreshold, LocalDate expiryDate) {
        this.name = name;
        this.form = form;
        this.stockQuantity = stockQuantity;
        this.lowStockThreshold = lowStockThreshold;
        this.expiryDate = expiryDate;
    }

    public String getName() {
        return name;
    }

    public String getForm() {
        return form;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public int getLowStockThreshold() {
        return lowStockThreshold;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public long getVersion() {
        return version;
    }

    public void dispense(int quantity) {
        if (quantity > stockQuantity) {
            throw new com.hospital.exception.InsufficientStockException("Insufficient medication stock");
        }
        stockQuantity -= quantity;
    }
}
