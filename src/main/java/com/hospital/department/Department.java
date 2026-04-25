package com.hospital.department;

import com.hospital.common.BaseEntity;
import com.hospital.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "departments")
@Getter
@Setter
public class Department extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String name;

    @Lob
    private String description;

    @Column(name = "floor_number")
    private Integer floorNumber;

    @Column(name = "building")
    private String building;

    @Column(name = "phone_extension")
    private String phoneExtension;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "head_doctor_id")
    private User headDoctor;

    @Column(name = "is_active")
    private boolean active = true;

    @Column(name = "consultation_rooms")
    private Integer consultationRooms;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}