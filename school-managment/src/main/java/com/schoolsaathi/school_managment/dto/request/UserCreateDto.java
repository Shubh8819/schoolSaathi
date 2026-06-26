package com.schoolsaathi.school_managment.dto.request;


import com.schoolsaathi.school_managment.enums.UserRole;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateDto {

    @NotBlank(message = "Name required")
    private String name;

    @NotBlank(message = "Email required")
    @Email(message = "Invalid email")
    private String email;

    @Pattern(regexp = "^[6-9]\\d{9}$",
            message = "Invalid mobile number")
    private String phone;

    @NotBlank(message = "Password required")
    @Size(min = 8, message = "Min 8 characters")
    private String password;

    @NotNull(message = "Role required")
    private UserRole role;

    // Teacher specific
    private String employeeId;
    private String designation;
    private LocalDate joiningDate;
}