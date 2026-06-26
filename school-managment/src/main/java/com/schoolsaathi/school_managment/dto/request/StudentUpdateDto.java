package com.schoolsaathi.school_managment.dto.request;



import com.schoolsaathi.school_managment.enums.Gender;
import com.schoolsaathi.school_managment.enums.StudentStatus;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentUpdateDto {

    private String name;
    private LocalDate dob;
    private Gender gender;
    private String bloodGroup;
    private String aadhaarNumber;
    private String category;
    private String religion;
    private String house;
    private UUID classId;
    private UUID sectionId;
    private Integer rollNumber;
    private String address;
    private String city;
    private String pincode;
    private String medicalCondition;
    private StudentStatus status;
    private LocalDate leavingDate;
    private String leavingReason;
}