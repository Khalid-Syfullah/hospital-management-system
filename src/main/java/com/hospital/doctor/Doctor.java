package com.hospital.doctor;

import com.hospital.common.BaseEntity;
import com.hospital.department.Department;
import com.hospital.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "doctors")
@Getter
@Setter
public class Doctor extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    private String specialization;

    private String qualification;

    private String licenseNumber;

    private LocalDate licenseExpiryDate;

    private Integer yearsOfExperience;

    @Lob
    @Column(name = "biography")
    private String biography;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @Column(name = "consultation_fee")
    private Double consultationFee;

    @Column(name = "follow_up_fee")
    private Double followUpFee;

    @ElementCollection
    @CollectionTable(name = "doctor_available_days", joinColumns = @JoinColumn(name = "doctor_id"))
    @Column(name = "day_of_week")
    private Set<String> availableDays = new HashSet<>();

    @Column(name = "slot_duration_minutes")
    private Integer slotDurationMinutes = 30;

    @Column(name = "max_patients_per_day")
    private Integer maxPatientsPerDay = 20;
}