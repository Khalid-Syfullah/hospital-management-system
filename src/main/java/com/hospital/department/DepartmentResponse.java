package com.hospital.department;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class DepartmentResponse {
    private UUID id;
    private String name;
    private String description;
    private Integer floorNumber;
    private String building;
    private String phoneExtension;
    private UUID headDoctorId;
    private String headDoctorName;
    private Boolean isActive;
    private Integer consultationRooms;
    private LocalDateTime createdAt;

    public static DepartmentResponse from(Department d) {
        return new DepartmentResponse(
                d.getId(),
                d.getName(),
                d.getDescription(),
                d.getFloorNumber(),
                d.getBuilding(),
                d.getPhoneExtension(),
                d.getHeadDoctor() != null ? d.getHeadDoctor().getId() : null,
                d.getHeadDoctor() != null ? d.getHeadDoctor().getFullName() : null,
                d.isActive(),
                d.getConsultationRooms(),
                d.getCreatedAt()
        );
    }
}