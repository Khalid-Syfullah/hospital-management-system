package com.hospital.ward;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class WardRequest {
    @NotBlank @Size(max = 100) private String name;
    @Size(max = 50) private String wardType;
    @Size(max = 10) private String floor;
    private String description;
    private UUID departmentId;
}
