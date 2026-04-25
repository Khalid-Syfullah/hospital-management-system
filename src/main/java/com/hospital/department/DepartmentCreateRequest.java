package com.hospital.department;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class DepartmentCreateRequest {
    @NotBlank(message = "Name is required")
    private String name;
    private String description;
    private Integer floorNumber;
    private String building;
    private String phoneExtension;
    private UUID headDoctorId;
    private Integer consultationRooms;
}