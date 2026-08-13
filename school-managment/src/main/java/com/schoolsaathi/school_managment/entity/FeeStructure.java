package com.schoolsaathi.school_managment.entity;





import com.schoolsaathi.school_managment.entity.baseentity.BaseEntity;
import com.schoolsaathi.school_managment.enums.FeeFrequency;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "fee_structures",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {
                                "school_id", "academic_year_id",
                                "class_id", "fee_head",

                        }
                )
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeeStructure extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id",insertable = false,updatable = false)
    private School school;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "academic_year_id")
    private AcademicYear academicYear;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id", nullable = false)
    private ClassRoom classRoom;

    @ManyToOne
    @JoinColumn(name = "template_id")
    private FeeTemplate template;






    // Fee Details
    @Column(name = "fee_head", nullable = false)
    private String feeHead;
    // Tuition Fee, Sports Fee, Library Fee

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private FeeFrequency frequency;
    // MONTHLY, QUARTERLY, HALF_YEARLY, ANNUALLY

    private LocalDate dueDate;
    private Integer dueDayOfMonth;  // Har month ki 10 tarikh

    @Column(name = "is_optional")
    private Boolean isOptional = false;

    private String description;

    @Override
    public String toString() {
        return "FeeStructure{" +
                "academicYear "+ academicYear +
                "feeHead='" + feeHead + '\'' +
                ", amount=" + amount +
                ", frequency=" + frequency +
                ", dueDate=" + dueDate +
                ", dueDayOfMonth=" + dueDayOfMonth +
                ", isOptional=" + isOptional +
                ", description='" + description + '\'' +
                '}';
    }
}