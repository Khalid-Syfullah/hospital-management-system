package com.hospital.department;

import com.hospital.common.BaseEntity;
import com.hospital.doctor.Doctor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "departments", indexes = @Index(name = "idx_department_name", columnList = "name", unique = true))
public class Department extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String name;
    @Column(length = 1000)
    private String description;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "head_doctor_id")
    private Doctor headDoctor;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Doctor getHeadDoctor() { return headDoctor; }
    public void setHeadDoctor(Doctor headDoctor) { this.headDoctor = headDoctor; }
}
