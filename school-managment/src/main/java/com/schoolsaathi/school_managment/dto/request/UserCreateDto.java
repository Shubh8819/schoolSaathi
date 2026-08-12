package com.schoolsaathi.school_managment.dto.request;

import com.schoolsaathi.school_managment.enums.UserRole;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDto {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Invalid 10-digit mobile number")
    private String phone;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @NotNull(message = "Role is required")
    private UserRole role;

    // Staff / Employee Specific
    private String employeeId;
    private String designation;
    private LocalDate joiningDate;

    // Department & Designation Links
    private UUID departmentId;
    private UUID designationId;
}