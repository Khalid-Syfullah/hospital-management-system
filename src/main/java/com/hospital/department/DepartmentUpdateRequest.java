package com.hospital.department;

import lombok.Data;

import java.util.UUID;

@Data
public class DepartmentUpdateRequest {
    private String name;
    private String description;
    private Integer floorNumber;
    private String building;
    private String phoneExtension;
    private UUID headDoctorId;
    private Integer consultationRooms;
}