package com.hospital.patient;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class InsuranceInfo {

    @Column(name = "insurance_provider", length = 100)
    private String provider;

    @Column(name = "insurance_policy_number", length = 50)
    private String policyNumber;

    @Column(name = "insurance_group_number", length = 50)
    private String groupNumber;

    @Column(name = "insurance_holder_name", length = 100)
    private String holderName;
}
