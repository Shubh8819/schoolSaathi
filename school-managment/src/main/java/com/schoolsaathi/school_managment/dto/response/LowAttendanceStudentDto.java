package com.schoolsaathi.school_managment.dto.response;

import lombok.*;
import java.util.UUID;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class LowAttendanceStudentDto {
    private UUID studentId;
    private String studentName;
    private String admissionNumber;
    private String className;
    private String sectionName;
    private Double attendancePercent;
}