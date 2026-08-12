package com.schoolsaathi.school_managment.dto.response;

import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class FeeDefaulterDto {
    private UUID studentId;
    private String studentName;
    private String admissionNumber;
    private String className;
    private String parentPhone;
    private BigDecimal pendingAmount;
}