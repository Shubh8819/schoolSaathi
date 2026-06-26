package com.schoolsaathi.school_managment.entity;

import com.schoolsaathi.school_managment.enums.BoardType;
import com.schoolsaathi.school_managment.enums.SchoolType;
import com.schoolsaathi.school_managment.enums.SubscriptionPlan;
import com.schoolsaathi.school_managment.entity.baseentity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "schools")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class School extends BaseEntity {

    // Identity
    @Column(name = "school_code",
            unique = true, nullable = false)
    private String schoolCode;

    @Column(nullable = false)
    private String name;

    private String tagline;

    // Contact
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    private String alternatePhone;
    private String website;

    // Address
    @Column(name = "address_line1")
    private String addressLine1;

    @Column(name = "address_line2")
    private String addressLine2;

    private String city;
    private String state;
    private String pincode;

    // School Details
    @Enumerated(EnumType.STRING)
    private BoardType boardType;

    @Enumerated(EnumType.STRING)
    private SchoolType schoolType;

    private String medium;
    private Integer establishedYear;
    private String affiliationNumber;
    private String udiseCode;

    // Principal Info
    private String principalName;
    private String principalPhone;
    private String principalEmail;

    // Subscription
    @Enumerated(EnumType.STRING)
    @Column(name = "subscription_plan")
    private SubscriptionPlan subscriptionPlan;

    private LocalDate trialStartDate;
    private LocalDate trialEndDate;
    private LocalDate subscriptionStart;
    private LocalDate subscriptionEnd;
    private Integer maxStudents;

    // Branding
    private String logoUrl;
    private String primaryColor;

    // Receipt Settings  ← NAYA ADD KIYA
    private String receiptPrefix;
    private Integer receiptStartNumber;
    private String financialYearFormat;

    // Bank Details  ← NAYA ADD KIYA
    private String bankName;
    private String accountNumber;
    private String ifscCode;
    private String bankBranch;

    // GST Details  ← NAYA ADD KIYA
    private String gstNumber;
    private String panNumber;

    // SMS/WhatsApp Config  ← NAYA ADD KIYA
    private String smsApiKey;
    private String whatsappApiKey;
    private Integer smsCreditBalance;

    // Status
    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "is_trial")
    private Boolean isTrial = true;

    // Relationships
    @OneToMany(mappedBy = "school",
            cascade = CascadeType.ALL)
    private List<User> users;

    @OneToMany(mappedBy = "school",
            cascade = CascadeType.ALL)
    private List<AcademicYear> academicYears;

    @OneToMany(mappedBy = "school",
            cascade = CascadeType.ALL)
    private List<ClassRoom> classRooms;
}