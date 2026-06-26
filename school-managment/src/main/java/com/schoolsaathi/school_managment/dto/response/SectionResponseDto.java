package com.schoolsaathi.school_managment.dto.response;


import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SectionResponseDto {

    private UUID id;
    private String name;
    private UUID classId;
    private String className;
    private String classTeacherName;
    private Integer capacity;
    private Integer totalStudents;    // Computed
}