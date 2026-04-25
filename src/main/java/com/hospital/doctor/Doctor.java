package com.hospital.doctor;

import com.hospital.common.BaseEntity;
import com.hospital.department.Department;
import com.hospital.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "doctors", indexes = @Index(name = "idx_doctor_license", columnList = "licenseNumber", unique = true))
public class Doctor extends BaseEntity {
    @Column(nullable = false)
    private String fullName;
    @Column(nullable = false, unique = true)
    private String licenseNumber;
    private String credentials;
    @ElementCollection
    private Set<String> specializations = new HashSet<>();
    @Column(length = 2000)
    private String availability;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }
    public String getCredentials() { return credentials; }
    public void setCredentials(String credentials) { this.credentials = credentials; }
    public Set<String> getSpecializations() { return specializations; }
    public void setSpecializations(Set<String> specializations) { this.specializations = specializations; }
    public String getAvailability() { return availability; }
    public void setAvailability(String availability) { this.availability = availability; }
    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
