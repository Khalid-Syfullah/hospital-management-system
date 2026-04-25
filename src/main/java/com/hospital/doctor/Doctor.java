package com.hospital.doctor;

import com.hospital.common.BaseEntity;
import com.hospital.department.Department;
import com.hospital.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "doctors", indexes = {
        @Index(name = "idx_doctors_user_id", columnList = "user_id"),
        @Index(name = "idx_doctors_department_id", columnList = "department_id")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doctor extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @Column(length = 100)
    private String specialization;

    @Column(length = 50)
    private String licenseNumber;

    @Column(length = 100)
    private String qualification;

    @Column
    private Integer yearsOfExperience;

    @Column(columnDefinition = "TEXT")
    private String biography;

    @Column(length = 100)
    private String consultationFee;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<DoctorAvailability> availabilitySlots = new ArrayList<>();
}
