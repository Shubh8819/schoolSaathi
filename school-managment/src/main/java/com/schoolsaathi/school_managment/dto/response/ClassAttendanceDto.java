package com.schoolsaathi.school_managment.dto.response;


import lombok.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ClassAttendanceDto {
    private String className;
    private Long presentCount;
    private Long totalCount;
    private Double attendancePercent;
}