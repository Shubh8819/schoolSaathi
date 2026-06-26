package com.schoolsaathi.school_managment.dto.response;


import com.schoolsaathi.school_managment.enums.UserRole;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {

    private UUID id;
    private String name;
    private String email;
    private String phone;
    private UserRole role;
    private String employeeId;
    private String designation;
    private LocalDate joiningDate;
    private Boolean isActive;
    private LocalDateTime lastLoginAt;
    private LocalDateTime createdAt;
}