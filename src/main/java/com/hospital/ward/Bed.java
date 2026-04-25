package com.hospital.ward;

import com.hospital.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "beds", indexes = {
        @Index(name = "idx_beds_ward_id", columnList = "ward_id"),
        @Index(name = "idx_beds_status", columnList = "status")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bed extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ward_id", nullable = false)
    private Ward ward;

    @Column(nullable = false, length = 20)
    private String bedNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private BedStatus status = BedStatus.AVAILABLE;

    @Column(length = 100)
    private String bedType;
}
