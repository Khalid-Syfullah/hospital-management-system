package com.hospital.ward;

import com.hospital.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "wards")
public class Ward extends BaseEntity {
    @Column(nullable = false) private String name;
    private String floor;
    public String getName() { return name; } public void setName(String name) { this.name = name; }
    public String getFloor() { return floor; } public void setFloor(String floor) { this.floor = floor; }
}
