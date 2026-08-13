package com.schoolsaathi.school_managment.entity;


import com.schoolsaathi.school_managment.entity.baseentity.BaseEntity;
import com.schoolsaathi.school_managment.enums.Relation;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "parents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Parent extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id",insertable = false,updatable = false)
    private School school;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    // Basic Info
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phone;

    private String alternatePhone;
    private String email;

    // Relation
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Relation relation;      // FATHER, MOTHER, GUARDIAN

    private String occupation;
    private String annualIncome;

    // WhatsApp
    private String whatsappNumber;

    @Column(name = "is_whatsapp_active")
    private Boolean isWhatsappActive = true;

    @Column(name = "is_primary")
    private Boolean isPrimary = false;
}
