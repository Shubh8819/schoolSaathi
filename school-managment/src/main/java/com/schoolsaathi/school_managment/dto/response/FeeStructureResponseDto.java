package com.schoolsaathi.school_managment.dto.response;


import com.schoolsaathi.school_managment.enums.FeeFrequency;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeeStructureResponseDto {

    private UUID id;
    private String academicYear;
    private UUID classId;
    private String className;
    private String feeHead;
    private BigDecimal amount;
    private FeeFrequency frequency;
    private LocalDate dueDate;
    private Integer dueDayOfMonth;
    private Boolean isOptional;
    private String description;
}