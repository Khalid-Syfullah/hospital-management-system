package com.hospital.doctor;

import java.util.Set;
import java.util.UUID;
import java.util.HashSet;

public record DoctorResponse(UUID id, String fullName, String licenseNumber, String credentials,
                             Set<String> specializations, String availability, UUID departmentId) {
    static DoctorResponse from(Doctor d) {
        return new DoctorResponse(d.getId(), d.getFullName(), d.getLicenseNumber(), d.getCredentials(),
                new HashSet<>(d.getSpecializations()), d.getAvailability(), d.getDepartment() == null ? null : d.getDepartment().getId());
    }
}
