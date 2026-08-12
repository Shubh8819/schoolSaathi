package com.schoolsaathi.school_managment.dto.request;

import com.schoolsaathi.school_managment.enums.Authority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DesignationDto {

    private UUID id;
    private UUID schoolId;
    
    @NotNull(message = "Department ID is required")
    private UUID departmentId;
    private String departmentName;

    @NotBlank(message = "Designation title is required")
    @Size(max = 100, message = "Title must not exceed 100 characters")
    private String title;

    @Size(max = 50, message = "Code must not exceed 50 characters")
    private String code;

    private String description;

    @Builder.Default
    private Boolean isActive = true;

    @Builder.Default
    private Set<Authority> authorities = new HashSet<>();

    private LocalDateTime createdAt;
}
