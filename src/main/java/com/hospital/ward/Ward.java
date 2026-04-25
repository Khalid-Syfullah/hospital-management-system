package com.hospital.ward;

import com.hospital.common.BaseEntity;
import com.hospital.department.Department;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "wards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ward extends BaseEntity {

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(length = 50)
    private String wardType;

    @Column(length = 10)
    private String floor;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToMany(mappedBy = "ward", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Bed> beds = new ArrayList<>();
}
