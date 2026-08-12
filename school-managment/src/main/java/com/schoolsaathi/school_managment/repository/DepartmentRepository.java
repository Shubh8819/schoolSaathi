package com.schoolsaathi.school_managment.repository;

import com.schoolsaathi.school_managment.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, UUID> {

    List<Department> findAllBySchoolIdAndIsDeletedFalse(UUID schoolId);

    List<Department> findAllByIsDeletedFalse();

    Optional<Department> findByIdAndIsDeletedFalse(UUID id);

    Optional<Department> findByIdAndSchoolIdAndIsDeletedFalse(UUID id, UUID schoolId);

    Boolean existsByNameAndSchoolIdAndIsDeletedFalse(String name, UUID schoolId);

    @Query("""
            SELECT d FROM Department d
            WHERE d.isDeleted = false
            AND (d.schoolId = :schoolId OR :schoolId IS NULL)
            AND (
                LOWER(d.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(d.code) LIKE LOWER(CONCAT('%', :keyword, '%'))
            )
            """)
    List<Department> searchDepartments(
            @Param("schoolId") UUID schoolId,
            @Param("keyword") String keyword
    );
}
