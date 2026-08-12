package com.schoolsaathi.school_managment.dto.response;


import lombok.*;
import java.util.UUID;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class AbsentStudentDto {
    private UUID studentId;
    private String studentName;
    private String className;
    private String sectionName;
}