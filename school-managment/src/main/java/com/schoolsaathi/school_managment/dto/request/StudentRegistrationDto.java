package com.schoolsaathi.school_managment.dto.request;



import com.schoolsaathi.school_managment.enums.Gender;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentRegistrationDto {

    // Identity
    private String admissionNumber;   // Auto generate hoga

    @NotBlank(message = "Student name required")
    private String name;

    private LocalDate dob;

    @NotNull(message = "Gender required")
    private Gender gender;

    private String bloodGroup;
    private String photoUrl;
    private String aadhaarNumber;
    private String aadhaarCardUrl;
    private String category;          // GENERAL, OBC, SC, ST, EWS
    private String religion;
    private String house;             // RED, BLUE, GREEN

    // Academic
    @NotNull(message = "Class required")
    private UUID classId;

    @NotNull(message = "Section required")
    private UUID sectionId;

    @NotNull(message = "Academic year required")
    private UUID academicYearId;

    private Integer rollNumber;

    // Contact / Address
    private String address;
    private String addressLine2;
    private String city;
    private String state;
    private String pincode;
    @Builder.Default
    private String country = "India";

    // Previous School
    private String previousSchool;
    private String admissionType;     // NEW, TRANSFER
    private String board;
    private String transferCertificateUrl;
    private String birthCertificateUrl;

    // Medical
    private String medicalCondition;

    // Sibling
    @Builder.Default
    private Boolean hasSibling = false;
    private UUID siblingStudentId;

    // Admission
    private LocalDate admissionDate;

    // Parents — student ke saath hi add honge
    @NotEmpty(message = "At least one parent required")
    private List<ParentDto> parents;
}
