// TeacherAttendanceDto.java
package com.schoolsaathi.school_managment.dto.response;

import lombok.*;

import java.util.UUID;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class TeacherAttendanceDto {
    private UUID teacherId;
    private String teacherName;
    private String designation;
    private String assignedClass;
    private String assignedSection;
    private String status;       // PRESENT, ABSENT, NOT_MARKED
    private String avatarInitials; // "RK" for Ramesh Kumar
}