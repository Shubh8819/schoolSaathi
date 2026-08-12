package com.schoolsaathi.school_managment.repository;

import com.schoolsaathi.school_managment.entity.AcademicYear;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface AcademicYearRepository extends JpaRepository<AcademicYear,UUID> {
    Optional<AcademicYear> findByIdAndSchoolIdAndIsDeletedFalse(@NotNull(message = "Academic year required") UUID academicYearId, UUID schoolId);

    Optional<List<AcademicYear>> findAllBySchoolIdAndIsDeletedFalse(UUID schoolId);
}
