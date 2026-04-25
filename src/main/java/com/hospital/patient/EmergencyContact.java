package com.hospital.patient;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class EmergencyContact {

    @Column(name = "emergency_name", length = 100)
    private String name;

    @Column(name = "emergency_relationship", length = 50)
    private String relationship;

    @Column(name = "emergency_phone", length = 20)
    private String phone;
}
