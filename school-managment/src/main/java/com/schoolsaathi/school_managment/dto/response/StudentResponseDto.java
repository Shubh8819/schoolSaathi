package com.schoolsaathi.school_managment.dto.response;


import com.schoolsaathi.school_managment.enums.Gender;
import com.schoolsaathi.school_managment.enums.StudentStatus;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponseDto {

    private UUID id;
    private String admissionNumber;
    private String name;
    private LocalDate dob;
    private Integer age;              // Computed
    private Gender gender;
    private String bloodGroup;
    private String category;
    private String house;
    private String photoUrl;

    // Academic
    private UUID classId;
    private String className;
    private UUID sectionId;
    private String sectionName;
    private String academicYear;
    private Integer rollNumber;

    // Contact
    private String address;
    private String city;

    // Status
    private StudentStatus status;
    private LocalDate admissionDate;

    // Parents
    private List<ParentResponseDto> parents;

    // Fee Summary
    private String totalFeesDue;      // Computed
    private String totalFeesPaid;     // Computed
    private String totalFeesPending;  // Computed

    // Attendance
    private String attendancePercent; // Computed
}