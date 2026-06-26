package com.schoolsaathi.school_managment.dto.response;


import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AcademicYearResponseDto {

    private UUID id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isCurrent;
    private Integer totalWorkingDays;  // Computed
}