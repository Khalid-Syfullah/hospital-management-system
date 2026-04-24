package com.hospital.department;

import com.hospital.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "departments")
public class Department extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String name;
    @Column(length = 1000)
    private String description;
    private String headOfDepartment;

    protected Department() {
    }

    public Department(String name, String description, String headOfDepartment) {
        this.name = name;
        this.description = description;
        this.headOfDepartment = headOfDepartment;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getHeadOfDepartment() {
        return headOfDepartment;
    }

    public void update(String name, String description, String headOfDepartment) {
        this.name = name;
        this.description = description;
        this.headOfDepartment = headOfDepartment;
    }
}
