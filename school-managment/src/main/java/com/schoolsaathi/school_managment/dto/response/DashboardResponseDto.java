// Updated DashboardResponseDto.java
package com.schoolsaathi.school_managment.dto.response;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class DashboardResponseDto {

    // ── School Overview ─────────────────────
    private Long totalStudents;
    private Long activeStudents;
    private Long totalClasses;
    private Long totalSections;

    // ── Teacher Stats ───────────────────────
    private Long totalTeachers;
    private Long teachersPresentToday;
    private Long teachersAbsentToday;
    private Long classesWithoutTeacher;

    // ── Student Attendance Overview ─────────
    private Long presentToday;
    private Long absentToday;
    private Double todayAttendancePercent;

    // ── Fee Overview (Principal level only) ─
    private BigDecimal feesCollectedThisMonth;
    private BigDecimal totalOutstandingFees;
    private Long studentsWithPendingFees;

    // ── Teacher List ─────────────────────────
    private List<TeacherAttendanceDto> teacherAttendanceList;

    // ── Class Coverage ───────────────────────
    private List<ClassCoverageDto> classCoverageList;

    // ── Monthly Fee Chart ────────────────────
    private List<MonthlyFeeDto> monthlyFeeCollection;

    // ── Alerts ───────────────────────────────
    private List<LowAttendanceStudentDto> lowAttendanceStudents;
    private List<FeeDefaulterDto> feeDefaulters;
}