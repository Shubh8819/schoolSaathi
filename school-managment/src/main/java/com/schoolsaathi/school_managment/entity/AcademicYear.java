package com.schoolsaathi.school_managment.entity;


import com.schoolsaathi.school_managment.entity.baseentity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "academic_years",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"school_id", "name"}
                )
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AcademicYear extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id",
            insertable = false,
            updatable = false)
    private School school;

    @Column(nullable = false)
    private String name;            // "2024-25"

    @Column(nullable = false)
    private LocalDate startDate;    // 2024-04-01

    @Column(nullable = false)
    private LocalDate endDate;      // 2025-03-31

    @Column(name = "is_current")
    private Boolean isCurrent = false;

    // Relationships
    @OneToMany(mappedBy = "academicYear",cascade = CascadeType.ALL)
    private List<FeeStructure> feeStructures;
}