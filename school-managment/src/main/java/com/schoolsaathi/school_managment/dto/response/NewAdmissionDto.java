package com.schoolsaathi.school_managment.dto.response;


import lombok.*;
import java.time.LocalDate;
import java.util.UUID;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class NewAdmissionDto {
    private UUID studentId;
    private String studentName;
    private String className;
    private LocalDate admissionDate;
}