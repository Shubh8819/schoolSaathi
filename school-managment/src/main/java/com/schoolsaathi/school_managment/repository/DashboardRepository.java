package com.schoolsaathi.school_managment.repository;

import com.schoolsaathi.school_managment.dto.response.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@org.springframework.stereotype.Repository
public interface DashboardRepository
        extends Repository
           <com.schoolsaathi.school_managment.entity.Student,
UUID> {

// ── Student Counts ──────────────────────
@Query("SELECT COUNT(s) FROM Student s WHERE s.schoolId = :schoolId AND s.isDeleted = false ")
Long countTotalStudents( @Param("schoolId") UUID schoolId);

@Query("SELECT COUNT(s) FROM Student s WHERE s.schoolId = :schoolId AND s.status = 'ACTIVE' AND s.isDeleted = false" )
Long countActiveStudents( @Param("schoolId") UUID schoolId);

// ── Class & Section Counts ──────────────
@Query("""
            SELECT COUNT(c) FROM ClassRoom c
            WHERE c.schoolId = :schoolId
            AND c.isDeleted = false
            """)
Long countTotalClasses(@Param("schoolId") UUID schoolId);

@Query("""
            SELECT COUNT(s) FROM Section s
            WHERE s.schoolId = :schoolId
            AND s.isDeleted = false
            """)
Long countTotalSections(@Param("schoolId") UUID schoolId);

// ── Teacher Counts ──────────────────────
@Query("""
            SELECT COUNT(u) FROM User u
            WHERE u.schoolId = :schoolId
            AND u.role = 'TEACHER'
            AND u.isActive = true
            AND u.isDeleted = false
            """)
Long countTotalTeachers(@Param("schoolId") UUID schoolId);

// ── Student Attendance ──────────────────
@Query("""
            SELECT COUNT(a) FROM Attendance a
            WHERE a.schoolId = :schoolId
            AND a.date = :today
            AND a.status = 'PRESENT'
            """)
Long countPresentToday(@Param("schoolId") UUID schoolId,@Param("today") LocalDate today);

@Query("""
            SELECT COUNT(a) FROM Attendance a
            WHERE a.schoolId = :schoolId
            AND a.date = :today
            AND a.status = 'ABSENT'
            """)
Long countAbsentToday(@Param("schoolId") UUID schoolId,@Param("today") LocalDate today);

// ── Fees ────────────────────────────────
@Query("""
            SELECT COALESCE(SUM(f.amountPaid), 0)
            FROM FeeCollection f
            WHERE f.schoolId = :schoolId
            AND MONTH(f.paymentDate) = :month
            AND YEAR(f.paymentDate) = :year
            AND f.isDeleted = false
            """)
BigDecimal sumFeesCollectedThisMonth(@Param("schoolId") UUID schoolId,@Param("month") int month,@Param("year") int year);

// ── Teacher List With Status ────────────
@Query(value = """
            SELECT
                u.id AS teacher_id,
                u.name AS teacher_name,
                u.designation,
                c.name AS assigned_class,
                sec.name AS assigned_section,
                COALESCE(
                    (SELECT 'PRESENT'
                     FROM attendance a
                     WHERE a.student_id IS NULL
                       AND a.school_id = u.school_id
                       AND a.date = :today
                     LIMIT 1),
                    'NOT_MARKED'
                ) AS status
            FROM users u
            LEFT JOIN classes c
                ON c.id = u.assigned_class_id
            LEFT JOIN sections sec
                ON sec.id = u.assigned_section_id
            WHERE u.school_id = :schoolId
            AND u.role = 'TEACHER'
            AND u.is_active = true
            AND u.is_deleted = false
            ORDER BY u.name
            """, nativeQuery = true)
List<Object[]> getTeacherListRaw(@Param("schoolId") UUID schoolId,@Param("today") LocalDate today);

// ── Class Coverage ──────────────────────
@Query(value = """
            SELECT
                c.id AS class_id,
                c.name AS class_name,
                sec.name AS section_name,
                u.name AS teacher_name,
                CASE
                    WHEN u.id IS NOT NULL THEN true
                    ELSE false
                END AS is_covered,
                COUNT(st.id) AS total_students
            FROM sections sec
            JOIN classes c ON c.id = sec.class_id
            LEFT JOIN users u
                ON u.assigned_section_id = sec.id
                AND u.is_active = true
                AND u.is_deleted = false
            LEFT JOIN students st
                ON st.section_id = sec.id
                AND st.status = 'ACTIVE'
                AND st.is_deleted = false
            WHERE c.school_id = :schoolId
            AND c.is_deleted = false
            AND sec.is_deleted = false
            GROUP BY c.id, c.name, sec.name,
                     u.name, u.id
            ORDER BY c.numeric_level, sec.name
            """, nativeQuery = true)
List<Object[]> getClassCoverageRaw(@Param("schoolId") UUID schoolId);

// ── Monthly Fee Chart ───────────────────
@Query(value = """
            SELECT
                TO_CHAR(payment_date, 'Mon') AS month,
                EXTRACT(YEAR FROM payment_date) AS year,
                SUM(amount_paid) AS amount
            FROM fee_collections
            WHERE school_id = :schoolId
            AND is_deleted = false
            AND payment_date >= :fromDate
            GROUP BY TO_CHAR(payment_date, 'Mon'),
                     EXTRACT(YEAR FROM payment_date),
                     EXTRACT(MONTH FROM payment_date)
            ORDER BY EXTRACT(MONTH FROM payment_date)
            """, nativeQuery = true)
List<Object[]> getMonthlyFeeCollectionRaw(@Param("schoolId") UUID schoolId,@Param("fromDate") LocalDate fromDate);

// ── Low Attendance ──────────────────────
@Query(value = """
            SELECT
                st.id,
                st.name,
                st.admission_number,
                c.name AS class_name,
                sec.name AS section_name,
                ROUND(
                    (COUNT(CASE WHEN a.status = 'PRESENT'
                           THEN 1 END) * 100.0
                    / NULLIF(COUNT(a.id), 0)), 2
                ) AS attendance_percent
            FROM students st
            JOIN classes c ON c.id = st.class_id
            JOIN sections sec ON sec.id = st.section_id
            LEFT JOIN attendance a
                ON a.student_id = st.id
            WHERE st.school_id = :schoolId
            AND st.status = 'ACTIVE'
            AND st.is_deleted = false
            GROUP BY st.id, st.name,
                     st.admission_number,
                     c.name, sec.name
            HAVING (COUNT(CASE WHEN a.status = 'PRESENT'
                          THEN 1 END) * 100.0
                   / NULLIF(COUNT(a.id), 0)) < 75
            ORDER BY attendance_percent ASC
            LIMIT 5
            """, nativeQuery = true)
List<Object[]> getLowAttendanceStudentsRaw(@Param("schoolId") UUID schoolId);

// ── Fee Defaulters ──────────────────────
@Query(value = """
            SELECT
                st.id,
                st.name,
                st.admission_number,
                c.name AS class_name,
                p.phone AS parent_phone,
                (COALESCE(SUM(fs.amount), 0)
                 - COALESCE(paid.total_paid, 0))
                    AS pending_amount
            FROM students st
            JOIN classes c ON c.id = st.class_id
            LEFT JOIN parents p
                ON p.student_id = st.id
                AND p.is_primary = true
            LEFT JOIN fee_structures fs
                ON fs.class_id = st.class_id
                AND fs.school_id = st.school_id
            LEFT JOIN (
                SELECT student_id,
                       SUM(amount_paid) AS total_paid
                FROM fee_collections
                WHERE is_deleted = false
                GROUP BY student_id
            ) paid ON paid.student_id = st.id
            WHERE st.school_id = :schoolId
            AND st.status = 'ACTIVE'
            AND st.is_deleted = false
            GROUP BY st.id, st.name,
                     st.admission_number,
                     c.name, p.phone, paid.total_paid
            HAVING (COALESCE(SUM(fs.amount), 0)
                   - COALESCE(paid.total_paid, 0)) > 0
            ORDER BY pending_amount DESC
            LIMIT 5
            """, nativeQuery = true)
List<Object[]> getFeeDefaultersRaw(@Param("schoolId") UUID schoolId);
}