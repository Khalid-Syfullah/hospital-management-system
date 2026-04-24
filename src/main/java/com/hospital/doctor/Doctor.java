package com.hospital.doctor;

import com.hospital.common.BaseEntity;
import com.hospital.department.Department;
import com.hospital.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "doctors")
public class Doctor extends BaseEntity {
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;
    @Column(nullable = false)
    private String fullName;
    @Column(nullable = false, unique = true)
    private String licenseNumber;
    private String specialization;
    @Column(length = 1000)
    private String availability;

    protected Doctor() {
    }

    public Doctor(String fullName, String licenseNumber, String specialization, String availability) {
        this.fullName = fullName;
        this.licenseNumber = licenseNumber;
        this.specialization = specialization;
        this.availability = availability;
    }

    public String getFullName() {
        return fullName;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public String getSpecialization() {
        return specialization;
    }

    public String getAvailability() {
        return availability;
    }

    public Department getDepartment() {
        return department;
    }

    public void update(String fullName, String licenseNumber, String specialization, String availability, Department department) {
        this.fullName = fullName;
        this.licenseNumber = licenseNumber;
        this.specialization = specialization;
        this.availability = availability;
        this.department = department;
    }
}
