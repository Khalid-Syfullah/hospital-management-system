package com.hospital.lab;

import com.hospital.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "lab_results")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LabResult extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lab_order_id", nullable = false)
    private LabOrder labOrder;

    @Column(columnDefinition = "TEXT")
    private String findings;

    @Column(columnDefinition = "TEXT")
    private String referenceRange;

    @Column(length = 50)
    private String interpretation;

    @Column
    private LocalDateTime resultDateTime;

    @Column(length = 100)
    private String performedBy;

    @Column(columnDefinition = "TEXT")
    private String attachmentPath;
}
