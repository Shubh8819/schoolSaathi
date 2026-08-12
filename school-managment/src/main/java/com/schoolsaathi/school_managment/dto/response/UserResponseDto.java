package com.schoolsaathi.school_managment.dto.response;

import com.schoolsaathi.school_managment.enums.Authority;
import com.schoolsaathi.school_managment.enums.UserRole;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {

    private UUID id;
    private UUID schoolId;
    private String name;
    private String email;
    private String phone;
    private UserRole role;
    private String employeeId;
    private String designation;
    private LocalDate joiningDate;
    private Boolean isActive;

    // Department & Designation
    private UUID departmentId;
    private String departmentName;
    private UUID designationId;
    private String designationTitle;

    // Inherited Authorities / Rights
    @Builder.Default
    private Set<Authority> authorities = new HashSet<>();

    private LocalDateTime lastLoginAt;
    private LocalDateTime createdAt;
}