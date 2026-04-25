package com.hospital.ward;

import com.hospital.department.Department;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class WardResponse {
    private UUID id;
    private String name;
    private String wardType;
    private UUID departmentId;
    private String departmentName;
    private Integer totalBeds;
    private Integer availableBeds;
    private boolean isActive;
    private LocalDateTime createdAt;

    public static WardResponse from(Ward w) {
        Department dept = w.getDepartment();
        return new WardResponse(
                w.getId(), w.getName(), w.getWardType(),
                dept != null ? dept.getId() : null,
                dept != null ? dept.getName() : null,
                w.getTotalBeds(), w.getAvailableBeds(), w.isActive(), w.getCreatedAt()
        );
    }
}