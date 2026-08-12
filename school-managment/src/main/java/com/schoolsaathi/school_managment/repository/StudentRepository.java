package com.schoolsaathi.school_managment.repository;

import com.schoolsaathi.school_managment.entity.Student;
import com.schoolsaathi.school_managment.enums.StudentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {

    // All tenant-scoped queries filter on schoolId (from BaseEntity) + deleted=false

    Optional<Student> findByIdAndSchoolIdAndIsDeletedFalse(UUID id, UUID schoolId);

    boolean existsByAdmissionNumberAndSchoolIdAndIsDeletedFalse(String admissionNumber, UUID schoolId);

    Page<Student> findAllBySchoolIdAndIsDeletedFalse(UUID schoolId, Pageable pageable);

    List<Student> findAllBySchoolIdAndClassRoomIdAndSectionIdAndIsDeletedFalse(
            UUID schoolId, UUID classRoomId, UUID sectionId);

    Page<Student> findAllBySchoolIdAndStatusAndIsDeletedFalse(UUID schoolId, StudentStatus status, Pageable pageable);

    @Query("""
           SELECT s FROM Student s
           WHERE s.schoolId = :schoolId
             AND s.isDeleted = false
             AND (LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
                  OR LOWER(s.admissionNumber) LIKE LOWER(CONCAT('%', :keyword, '%')))
           """)
    Page<Student> searchBySchoolId(@Param("schoolId") UUID schoolId,
                                    @Param("keyword") String keyword,
                                    Pageable pageable);

    long countBySchoolIdAndIsDeletedFalse(UUID schoolId);
}
