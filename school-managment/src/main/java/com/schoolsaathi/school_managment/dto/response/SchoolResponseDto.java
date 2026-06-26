package com.schoolsaathi.school_managment.dto.response;



import com.schoolsaathi.school_managment.enums.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SchoolResponseDto {

    private UUID id;
    private String schoolCode;
    private String name;
    private String tagline;
    private String email;
    private String phone;
    private String city;
    private String state;
    private String pincode;
    private BoardType boardType;
    private SchoolType schoolType;
    private String medium;
    private String principalName;
    private String principalPhone;
    private SubscriptionPlan subscriptionPlan;
    private LocalDate subscriptionEnd;
    private Boolean isActive;
    private Boolean isTrial;
    private LocalDate trialEndDate;
    private String logoUrl;
    private String primaryColor;
    private String receiptPrefix;

    // Dashboard computed fields
    private Integer totalStudents;
    private Integer totalTeachers;
    private String currentAcademicYear;

    private LocalDateTime createdAt;
}