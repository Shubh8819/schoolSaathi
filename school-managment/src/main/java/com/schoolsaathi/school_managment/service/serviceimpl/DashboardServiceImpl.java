package com.schoolsaathi.school_managment.service.serviceimpl;

import com.schoolsaathi.school_managment.dto.response.*;
import com.schoolsaathi.school_managment.repository.DashboardRepository;
import com.schoolsaathi.school_managment.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final DashboardRepository dashboardRepository;

    @Override
    public DashboardResponseDto getSchoolAdminDashboard(UUID schoolId) {
        log.info("Building principal dashboard: {}",schoolId);
        LocalDate today = LocalDate.now();
        int month = today.getMonthValue();
        int year  = today.getYear();

        // ── School Overview ─────────────────
        Long totalStudents = dashboardRepository.countTotalStudents(schoolId);
        Long activeStudents = dashboardRepository.countActiveStudents(schoolId);
        Long totalClasses = dashboardRepository.countTotalClasses(schoolId);
        Long totalSections = dashboardRepository.countTotalSections(schoolId);
        Long totalTeachers = dashboardRepository.countTotalTeachers(schoolId);

        // ── Student Attendance ──────────────
        Long presentToday = dashboardRepository.countPresentToday(schoolId, today);
        Long absentToday  = dashboardRepository.countAbsentToday(schoolId, today);
        Long total = presentToday + absentToday;
        Double attendancePct =total > 0? Math.round((presentToday * 100.0 / total)* 100.0) / 100.0: 0.0;

        // ── Fees ─────────────────────────────
        BigDecimal feesThisMonth = dashboardRepository.sumFeesCollectedThisMonth(schoolId, month, year);

        // ── Teacher List ─────────────────────
        List<TeacherAttendanceDto> teacherList = buildTeacherList(schoolId, today);

        long presentTeachers = teacherList.stream().filter(t -> "PRESENT".equals(t.getStatus())).count();
        long absentTeachers = teacherList.stream().filter(t -> "ABSENT".equals(t.getStatus())).count();

        // ── Class Coverage ───────────────────
        List<ClassCoverageDto> coverage = buildClassCoverage(schoolId);

        long uncoveredClasses = coverage.stream().filter(c -> !c.getIsCovered()).count();

        // ── Charts & Alerts ──────────────────
        List<MonthlyFeeDto> monthlyFees = buildMonthlyFeeChart(schoolId);
        List<LowAttendanceStudentDto> lowAtt = buildLowAttendanceList(schoolId);
        List<FeeDefaulterDto> defaulters = buildFeeDefaultersList(schoolId);

        return DashboardResponseDto.builder()
                .totalStudents(totalStudents)
                .activeStudents(activeStudents)
                .totalClasses(totalClasses)
                .totalSections(totalSections)
                .totalTeachers(totalTeachers)
                .teachersPresentToday(presentTeachers)
                .teachersAbsentToday(absentTeachers)
                .classesWithoutTeacher(uncoveredClasses)
                .presentToday(presentToday)
                .absentToday(absentToday)
                .todayAttendancePercent(attendancePct)
                .feesCollectedThisMonth(feesThisMonth)
                .totalOutstandingFees(
                        calculateOutstanding(defaulters))
                .studentsWithPendingFees(
                        (long) defaulters.size())
                .teacherAttendanceList(teacherList)
                .classCoverageList(coverage)
                .monthlyFeeCollection(monthlyFees)
                .lowAttendanceStudents(lowAtt)
                .feeDefaulters(defaulters)
                .build();
    }

    // ─── Teacher List Builder ────────────────
    private List<TeacherAttendanceDto> buildTeacherList(UUID schoolId,LocalDate today) {

        return dashboardRepository.getTeacherListRaw(schoolId, today)
                .stream()
                .map(row -> {
                    String name = row[1] != null ? (String) row[1] : "";
                    String initials = getInitials(name);
                    return TeacherAttendanceDto.builder()
                            .teacherId(row[0] != null ? UUID.fromString(row[0].toString()): null)
                            .teacherName(name)
                            .designation(row[2] != null ? (String) row[2] : "Teacher")
                            .assignedClass(row[3] != null ? (String) row[3] : "—")
                            .assignedSection(row[4] != null ? (String) row[4] : "—")
                            .status(row[5] != null ? (String) row[5] : "NOT_MARKED")
                            .avatarInitials(initials)
                            .build();
                })
                .collect(Collectors.toList());
    }

    // ─── Class Coverage Builder ──────────────
    private List<ClassCoverageDto>buildClassCoverage(UUID schoolId) {

        return dashboardRepository.getClassCoverageRaw(schoolId)
                .stream()
                .map(row ->
                        ClassCoverageDto.builder()
                                .classId(row[0] != null ? UUID.fromString( row[0].toString()) : null)
                                .className(row[1] != null ? (String) row[1] : "")
                                .sectionName(row[2] != null ? (String) row[2] : "")
                                .teacherName( row[3] != null ? (String) row[3] : null)
                                .isCovered(row[4] != null && (Boolean) row[4])
                                .totalStudents(row[5] != null ? ((Number) row[5]).longValue() : 0L)
                                .build())
                .collect(Collectors.toList());
    }

    // ─── Monthly Fee Chart Builder ───────────
    private List<MonthlyFeeDto> buildMonthlyFeeChart(UUID schoolId) {

        LocalDate from = LocalDate.now().minusMonths(5).withDayOfMonth(1);

        return dashboardRepository.getMonthlyFeeCollectionRaw(schoolId, from)
                .stream()
                .map(row -> MonthlyFeeDto.builder()
                        .month(((String) row[0]).trim())
                        .year(((Number) row[1]).intValue())
                        .amount(row[2] instanceof BigDecimal? (BigDecimal) row[2]: BigDecimal.valueOf(((Number) row[2]).doubleValue()))
                        .build())
                .collect(Collectors.toList());
    }

    // ─── Low Attendance Builder ──────────────
    private List<LowAttendanceStudentDto> buildLowAttendanceList(UUID schoolId) {

        return dashboardRepository
                .getLowAttendanceStudentsRaw(schoolId)
                .stream()
                .map(row ->
                        LowAttendanceStudentDto.builder()
                                .studentId(row[0] != null ? UUID.fromString(row[0].toString()) : null)
                                .studentName(String.valueOf( row[1]))
                                .admissionNumber(String.valueOf( row[2]))
                                .className(String.valueOf( row[3]))
                                .sectionName((String) row[4])
                                .attendancePercent(row[5] != null ? ((Number) row[5]).doubleValue() : 0.0)
                                .build())
                .collect(Collectors.toList());
    }

    // ─── Fee Defaulters Builder ──────────────
    private List<FeeDefaulterDto>
    buildFeeDefaultersList(UUID schoolId) {

        return dashboardRepository.getFeeDefaultersRaw(schoolId)
                .stream()
                .map(row ->FeeDefaulterDto.builder()
                                .studentId(row[0] != null ? UUID.fromString(row[0].toString()): null)
                                .studentName(String.valueOf(  row[1]))
                                .admissionNumber(String.valueOf( row[2]))
                                .className(String.valueOf( row[3]))
                                .parentPhone(row[4] != null ? (String) row[4] : "N/A")
                                .pendingAmount(row[5] instanceof BigDecimal ? (BigDecimal) row[5]: BigDecimal.valueOf(((Number) row[5]).doubleValue()))
                                .build())
                .collect(Collectors.toList());
    }

    // ─── Helpers ────────────────────────────
    private BigDecimal calculateOutstanding(List<FeeDefaulterDto> list) {
        return list.stream()
                .map(FeeDefaulterDto::getPendingAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private String getInitials(String name) {
        if (name == null || name.isBlank()) return "?";
        String[] parts = name.trim().split("\\s+");
        if (parts.length == 1)
            return parts[0].substring(0, 1).toUpperCase();
        return (parts[0].substring(0, 1) + parts[parts.length - 1].substring(0, 1)).toUpperCase();
    }
}