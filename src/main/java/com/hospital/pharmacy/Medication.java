package com.hospital.pharmacy;

import com.hospital.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.time.LocalDate;

@Entity
@Table(name = "medications", indexes = @Index(name = "idx_medication_name", columnList = "name"))
public class Medication extends BaseEntity {
    @Column(nullable = false) private String name;
    private String strength;
    private String form;
    @Column(nullable = false) private int stockQuantity;
    @Column(nullable = false) private int lowStockThreshold = 10;
    private LocalDate expiryDate;
    @Version private long version;
    public String getName() { return name; } public void setName(String name) { this.name = name; }
    public String getStrength() { return strength; } public void setStrength(String strength) { this.strength = strength; }
    public String getForm() { return form; } public void setForm(String form) { this.form = form; }
    public int getStockQuantity() { return stockQuantity; } public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }
    public int getLowStockThreshold() { return lowStockThreshold; } public void setLowStockThreshold(int lowStockThreshold) { this.lowStockThreshold = lowStockThreshold; }
    public LocalDate getExpiryDate() { return expiryDate; } public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
}
