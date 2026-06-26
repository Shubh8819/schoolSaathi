package com.schoolsaathi.school_managment.dto.request;



import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AcademicYearDto {

    @NotBlank(message = "Name required")
    private String name;              // "2024-25"

    @NotNull(message = "Start date required")
    private LocalDate startDate;      // 2024-04-01

    @NotNull(message = "End date required")
    private LocalDate endDate;        // 2025-03-31

    private Boolean isCurrent = false;
}