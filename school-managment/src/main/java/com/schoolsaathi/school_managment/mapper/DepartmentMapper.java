package com.schoolsaathi.school_managment.mapper;

import com.schoolsaathi.school_managment.dto.request.DepartmentDto;
import com.schoolsaathi.school_managment.dto.request.DesignationDto;
import com.schoolsaathi.school_managment.entity.Department;
import com.schoolsaathi.school_managment.entity.Designation;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DepartmentMapper {

    public DepartmentDto toDepartmentDto(Department department) {
        if (department == null) {
            return null;
        }

        List<DesignationDto> designationDtos = department.getDesignations() != null
                ? department.getDesignations().stream()
                .filter(d -> d.getIsDeleted() == null || !d.getIsDeleted())
                .map(this::toDesignationDto)
                .collect(Collectors.toList())
                : Collections.emptyList();

        return DepartmentDto.builder()
                .id(department.getId())
                .schoolId(department.getSchoolId())
                .name(department.getName())
                .code(department.getCode())
                .description(department.getDescription())
                .isActive(department.getIsActive())
                .designationCount(designationDtos.size())
                .designations(designationDtos)
                .createdAt(department.getCreatedAt())
                .build();
    }

    public Department toDepartmentEntity(DepartmentDto dto) {
        if (dto == null) {
            return null;
        }

        Department department = Department.builder()
                .name(dto.getName())
                .code(dto.getCode())
                .description(dto.getDescription())
                .isActive(dto.getIsActive() != null ? dto.getIsActive() : true)
                .build();
        
        department.setSchoolId(dto.getSchoolId());

        return department;
    }

    public DesignationDto toDesignationDto(Designation designation) {
        if (designation == null) {
            return null;
        }

        return DesignationDto.builder()
                .id(designation.getId())
                .schoolId(designation.getSchoolId())
                .departmentId(designation.getDepartment() != null ? designation.getDepartment().getId() : null)
                .departmentName(designation.getDepartment() != null ? designation.getDepartment().getName() : null)
                .title(designation.getTitle())
                .code(designation.getCode())
                .description(designation.getDescription())
                .isActive(designation.getIsActive())
                .authorities(designation.getAuthorities() != null ? new HashSet<>(designation.getAuthorities()) : new HashSet<>())
                .createdAt(designation.getCreatedAt())
                .build();
    }

    public Designation toDesignationEntity(DesignationDto dto, Department department) {
        if (dto == null) {
            return null;
        }

        Designation designation = Designation.builder()
                .title(dto.getTitle())
                .code(dto.getCode())
                .description(dto.getDescription())
                .isActive(dto.getIsActive() != null ? dto.getIsActive() : true)
                .department(department)
                .authorities(dto.getAuthorities() != null ? new HashSet<>(dto.getAuthorities()) : new HashSet<>())
                .build();
        
        designation.setSchoolId(dto.getSchoolId());
        return designation;
    }
}
