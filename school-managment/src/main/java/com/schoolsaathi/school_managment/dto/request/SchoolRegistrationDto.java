package com.schoolsaathi.school_managment.dto.request;



import com.schoolsaathi.school_managment.enums.BoardType;
import com.schoolsaathi.school_managment.enums.SchoolType;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SchoolRegistrationDto {

    // Basic Info
    @NotBlank(message = "School name required")
    private String name;

    private String tagline;

    @NotBlank(message = "Email required")
    @Email(message = "Invalid email")
    private String email;

    @NotBlank(message = "Phone required")
    @Pattern(regexp = "^[6-9]\\d{9}$",
            message = "Invalid Indian mobile number")
    private String phone;

    private String alternatePhone;
    private String website;

    // Address
    @NotBlank(message = "Address required")
    private String addressLine1;

    private String addressLine2;

    @NotBlank(message = "City required")
    private String city;

    @NotBlank(message = "State required")
    private String state;

    @NotBlank(message = "Pincode required")
    @Pattern(regexp = "^[1-9][0-9]{5}$",
            message = "Invalid pincode")
    private String pincode;

    // School Details
    @NotNull(message = "Board type required")
    private BoardType boardType;

    @NotNull(message = "School type required")
    private SchoolType schoolType;

    private String medium;
    private Integer establishedYear;
    private String affiliationNumber;
    private String udiseCode;

    // Principal
    @NotBlank(message = "Principal name required")
    private String principalName;

    private String principalPhone;
    private String principalEmail;

    // First Admin Account
    @NotBlank(message = "Admin name required")
    private String adminName;

    @NotBlank(message = "Admin email required")
    @Email(message = "Invalid admin email")
    private String adminEmail;

    @NotBlank(message = "Password required")
    @Size(min = 8, message = "Min 8 characters")
    private String adminPassword;
}