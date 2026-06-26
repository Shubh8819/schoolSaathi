package com.schoolsaathi.school_managment.dto.request;


import com.schoolsaathi.school_managment.enums.FeeFrequency;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeeStructureDto {

    @NotNull(message = "Academic year required")
    private UUID academicYearId;

    @NotNull(message = "Class required")
    private UUID classId;

    @NotBlank(message = "Fee head required")
    private String feeHead;
    // "Tuition Fee", "Sports Fee"

    @NotNull(message = "Amount required")
    @DecimalMin(value = "0.0",
            message = "Amount cannot be negative")
    private BigDecimal amount;

    @NotNull(message = "Frequency required")
    private FeeFrequency frequency;

    private LocalDate dueDate;
    private Integer dueDayOfMonth;
    private Boolean isOptional = false;
    private String description;
}