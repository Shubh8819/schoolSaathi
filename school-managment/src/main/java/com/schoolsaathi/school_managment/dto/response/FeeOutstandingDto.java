package com.schoolsaathi.school_managment.dto.response;



import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

// Pending fees report ke liye
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeeOutstandingDto {

    private UUID studentId;
    private String studentName;
    private String admissionNumber;
    private String className;
    private String sectionName;
    private String parentPhone;

    // Fee Summary
    private BigDecimal totalFeesDue;
    private BigDecimal totalFeesPaid;
    private BigDecimal totalPending;
    private BigDecimal totalDiscount;
}