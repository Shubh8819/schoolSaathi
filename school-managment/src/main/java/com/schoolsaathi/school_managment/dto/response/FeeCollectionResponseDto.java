package com.schoolsaathi.school_managment.dto.response;


import com.schoolsaathi.school_managment.enums.PaymentMode;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeeCollectionResponseDto {

    private UUID id;
    private String receiptNumber;

    // Student Info
    private UUID studentId;
    private String studentName;
    private String admissionNumber;
    private String className;
    private String sectionName;

    // Payment Info
    private BigDecimal amountPaid;
    private BigDecimal discountAmount;
    private BigDecimal fineAmount;
    private BigDecimal totalAmount;
    private LocalDate paymentDate;
    private PaymentMode paymentMode;

    // Fee Details
    private String feeHead;
    private String feeMonth;
    private String academicYear;

    // Cheque
    private String chequeNumber;
    private String bankName;

    // Online
    private String transactionId;

    // Partial
    private Boolean isPartialPayment;
    private BigDecimal balanceAmount;

    // Collected By
    private String collectedByName;

    private String remarks;
    private LocalDateTime createdAt;
}