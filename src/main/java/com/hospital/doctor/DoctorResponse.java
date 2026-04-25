package com.hospital.doctor;

import com.hospital.common.ApiResponse;
import com.hospital.common.PageResponse;
import com.hospital.department.Department;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class DoctorResponse {
    private UUID id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String specialization;
    private String qualification;
    private String licenseNumber;
    private LocalDate licenseExpiryDate;
    private Integer yearsOfExperience;
    private String biography;
    private UUID departmentId;
    private String departmentName;
    private Double consultationFee;
    private Double followUpFee;
    private Integer slotDurationMinutes;
    private Integer maxPatientsPerDay;
    private LocalDateTime createdAt;

    public static DoctorResponse from(Doctor doctor) {
        Department dept = doctor.getDepartment();
        return new DoctorResponse(
                doctor.getId(),
                doctor.getUser().getEmail(),
                doctor.getUser().getFirstName(),
                doctor.getUser().getLastName(),
                doctor.getUser().getPhone(),
                doctor.getSpecialization(),
                doctor.getQualification(),
                doctor.getLicenseNumber(),
                doctor.getLicenseExpiryDate(),
                doctor.getYearsOfExperience(),
                doctor.getBiography(),
                dept != null ? dept.getId() : null,
                dept != null ? dept.getName() : null,
                doctor.getConsultationFee(),
                doctor.getFollowUpFee(),
                doctor.getSlotDurationMinutes(),
                doctor.getMaxPatientsPerDay(),
                doctor.getCreatedAt()
        );
    }
}