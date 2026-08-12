package com.schoolsaathi.school_managment.dto.request;

import com.schoolsaathi.school_managment.enums.UserRole;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDto {

    private String name;
    private String phone;
    private UserRole role;
    private String employeeId;
    private String designation;
    private LocalDate joiningDate;
    private Boolean isActive;

    // Department & Designation Links
    private UUID departmentId;
    private UUID designationId;
}