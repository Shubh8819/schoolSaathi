package com.schoolsaathi.school_managment.repository;

import com.schoolsaathi.school_managment.entity.Designation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DesignationRepository extends JpaRepository<Designation, UUID> {

    List<Designation> findAllByDepartmentIdAndIsDeletedFalse(UUID departmentId);

    List<Designation> findAllBySchoolIdAndIsDeletedFalse(UUID schoolId);

    Optional<Designation> findByIdAndIsDeletedFalse(UUID id);

    Boolean existsByTitleAndDepartmentIdAndIsDeletedFalse(String title, UUID departmentId);
}
