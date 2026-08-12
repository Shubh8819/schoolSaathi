package com.schoolsaathi.school_managment.mapper;

import com.schoolsaathi.school_managment.dto.response.UserResponseDto;
import com.schoolsaathi.school_managment.entity.User;
import com.schoolsaathi.school_managment.enums.Authority;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class UserMapper {

    public UserResponseDto toDto(User user) {
        if (user == null) {
            return null;
        }

        Set<Authority> authorities = new HashSet<>();
        if (user.getDesignationEntity() != null && user.getDesignationEntity().getAuthorities() != null) {
            authorities.addAll(user.getDesignationEntity().getAuthorities());
        }

        return UserResponseDto.builder()
                .id(user.getId())
                .schoolId(user.getSchoolId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .employeeId(user.getEmployeeId())
                .designation(user.getDesignation() != null ? user.getDesignation() : (user.getDesignationEntity() != null ? user.getDesignationEntity().getTitle() : null))
                .joiningDate(user.getJoiningDate())
                .isActive(user.getIsActive())
                .departmentId(user.getDepartment() != null ? user.getDepartment().getId() : null)
                .departmentName(user.getDepartment() != null ? user.getDepartment().getName() : null)
                .designationId(user.getDesignationEntity() != null ? user.getDesignationEntity().getId() : null)
                .designationTitle(user.getDesignationEntity() != null ? user.getDesignationEntity().getTitle() : null)
                .authorities(authorities)
                .lastLoginAt(user.getLastLoginAt())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
