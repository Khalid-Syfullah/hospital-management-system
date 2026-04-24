package com.hospital.ward;

import com.hospital.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "wards")
public class Ward extends BaseEntity {
    @Column(nullable = false)
    private String name;
    private String type;

    protected Ward() {
    }

    public Ward(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
