package com.schoolsaathi.school_managment.dto.response;


import lombok.*;

import java.util.UUID;

// Monthly report ke liye
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceReportDto {

    private UUID studentId;
    private String studentName;
    private String admissionNumber;
    private String rollNumber;

    // Counts
    private Integer totalDays;
    private Integer presentDays;
    private Integer absentDays;
    private Integer lateDays;
    private Integer halfDays;

    // Percentage
    private Double attendancePercent;
    private String attendanceStatus;  // GOOD, LOW, CRITICAL
}