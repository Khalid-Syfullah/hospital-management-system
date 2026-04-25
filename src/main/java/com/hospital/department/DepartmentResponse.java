package com.hospital.department;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class DepartmentResponse {
    private UUID id;
    private String name;
    private String description;
    private String headOfDepartment;
    private String phoneExtension;
    private String floor;
    private LocalDateTime createdAt;
}
