package com.hospital.ward;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class WardResponse {
    private UUID id;
    private String name;
    private String wardType;
    private String floor;
    private String description;
    private UUID departmentId;
    private String departmentName;
    private long availableBeds;
    private long occupiedBeds;
    private LocalDateTime createdAt;
}
