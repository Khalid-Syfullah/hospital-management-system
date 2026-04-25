package com.hospital.doctor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorResponse {
    private UUID id;
    private String firstName;
    private String lastName;
    private String licenseNumber;
    private String specialization;
    private String phoneNumber;
    private String email;
    private UUID departmentId;
    private String departmentName;
    private boolean available;
    private String availability;
    private boolean active;
}
