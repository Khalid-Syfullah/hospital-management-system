package com.hospital.ward;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class WardCreateRequest {
    @NotBlank private String name;
    private String wardType;
    @NotNull private Integer totalBeds;
    private UUID departmentId;
}