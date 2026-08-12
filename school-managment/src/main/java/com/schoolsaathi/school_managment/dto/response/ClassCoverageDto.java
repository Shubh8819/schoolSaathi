// ClassCoverageDto.java
package com.schoolsaathi.school_managment.dto.response;

import lombok.*;
import java.util.UUID;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ClassCoverageDto {
    private UUID classId;
    private String className;
    private String sectionName;
    private String teacherName;   // Null if no teacher
    private Boolean isCovered;    // Teacher present ya nahi
    private Long totalStudents;
}