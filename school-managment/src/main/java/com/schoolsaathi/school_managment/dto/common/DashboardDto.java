package com.schoolsaathi.school_managment.dto.common;


import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDto {

    // Students
    private Integer totalStudents;
    private Integer activeStudents;
    private Integer newAdmissionsThisMonth;

    // Fees
    private BigDecimal feesCollectedToday;
    private BigDecimal feesCollectedThisMonth;
    private BigDecimal totalPendingFees;
    private Integer studentsWithPendingFees;

    // Attendance
    private Double todayAttendancePercent;
    private Integer presentToday;
    private Integer absentToday;

    // Staff
    private Integer totalTeachers;
    private Integer totalStaff;

    // Messages
    private Integer messagesSentThisMonth;
}