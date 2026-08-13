package com.schoolsaathi.school_managment.entity;


import com.schoolsaathi.school_managment.entity.baseentity.BaseEntity;
import com.schoolsaathi.school_managment.enums.AdmissionCategory;
import com.schoolsaathi.school_managment.enums.Gender;
import com.schoolsaathi.school_managment.enums.StudentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "students",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"school_id", "admission_number"}
                )
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id",insertable = false,updatable = false)
    private School school;

    // Identity
    @Column(name = "admission_number", nullable = false)
    private String admissionNumber;     // 2024001

    @Column(nullable = false)
    private String name;

    private LocalDate dob;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String bloodGroup;
    private String photoUrl;
    private String religion;
    private String house;               // RED, BLUE, GREEN

    // Academic
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id")
    private ClassRoom classRoom;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "academic_year_id")
    private AcademicYear academicYear;

    private Integer rollNumber;

    // Contact / Address
    private String address;
    private String addressLine2;
    private String city;
    private String state;
    private String pincode;

    @Builder.Default
    private String country = "India";

    // Admission
    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private AdmissionCategory category;

    private String board;
    private String admissionType;       // NEW, TRANSFER

    // Previous School
    private String previousSchool;
    private String transferCertificate;
    private String birthCertificate;

    // Government ID
    private String aadharNumber;
    private String aadharCardUrl;

    // Medical
    private String medicalCondition;

    // Sibling
    @Builder.Default
    private Boolean hasSibling = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sibling_student_id")
    private Student sibling;

    // Status
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StudentStatus status = StudentStatus.ACTIVE;

    private LocalDate admissionDate;
    private LocalDate leavingDate;
    private String leavingReason;

    // Relationships
    @OneToMany(mappedBy = "student",cascade = CascadeType.ALL)
    private List<Parent> parents;

    @OneToMany(mappedBy = "student",cascade = CascadeType.ALL)
    private List<FeeCollection> feeCollections;

    @OneToMany(mappedBy = "student",cascade = CascadeType.ALL)
    private List<Attendance> attendances;
}
