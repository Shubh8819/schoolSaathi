package com.schoolsaathi.school_managment.service;

import com.schoolsaathi.school_managment.dto.request.DepartmentDto;
import com.schoolsaathi.school_managment.dto.request.DesignationDto;

import java.util.List;
import java.util.UUID;

public interface DepartmentService {

    // Department Operations
    DepartmentDto createDepartment(DepartmentDto dto, UUID schoolId);

    DepartmentDto updateDepartment(UUID departmentId, DepartmentDto dto);

    DepartmentDto getDepartmentById(UUID departmentId);

    List<DepartmentDto> getAllDepartments(UUID schoolId);

    List<DepartmentDto> searchDepartments(UUID schoolId, String keyword);

    void deleteDepartment(UUID departmentId);

    DepartmentDto toggleDepartmentStatus(UUID departmentId);

    // Designation Operations
    DesignationDto createDesignation(DesignationDto dto, UUID schoolId);

    DesignationDto updateDesignation(UUID designationId, DesignationDto dto);

    DesignationDto getDesignationById(UUID designationId);

    List<DesignationDto> getDesignationsByDepartment(UUID departmentId);

    void deleteDesignation(UUID designationId);

    DesignationDto toggleDesignationStatus(UUID designationId);
}
