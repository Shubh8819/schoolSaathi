package com.schoolsaathi.school_managment.dto.request;

import com.schoolsaathi.school_managment.entity.baseentity.BaseEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DepartmentDto extends BaseEntity {

    private UUID id;
    private UUID schoolId;

    @NotBlank(message = "Department name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @Size(max = 50, message = "Code must not exceed 50 characters")
    private String code;

    private String description;

    @Builder.Default
    private Boolean isActive = true;

    private Integer designationCount;
    private List<DesignationDto> designations;
    private LocalDateTime createdAt;
}
