package com.schoolsaathi.school_managment.dto.response;

import com.schoolsaathi.school_managment.enums.StudentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponseDto {
    private UUID id;
    private String admissionNumber;
    private String fullName;
    private String name;
    private String className;
    private String sectionName;
    private Integer rollNumber;
    private String photoUrl;
    private StudentStatus status;
    private LocalDate admissionDate;
}
