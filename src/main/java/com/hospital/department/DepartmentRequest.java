package com.hospital.department;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DepartmentRequest {

    @NotBlank
    @Size(max = 100)
    private String name;

    private String description;

    @Size(max = 100)
    private String headOfDepartment;

    @Size(max = 20)
    private String phoneExtension;

    @Size(max = 10)
    private String floor;
}
