package com.schoolsaathi.school_managment.dto.request;


import com.schoolsaathi.school_managment.enums.PaymentMode;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeeCollectionDto {

    @NotNull(message = "Student required")
    private UUID studentId;

    private UUID feeStructureId;

    @NotNull(message = "Academic year required")
    private UUID academicYearId;

    @NotNull(message = "Amount required")
    @DecimalMin(value = "1.0",
            message = "Amount must be greater than 0")
    private BigDecimal amountPaid;

    private BigDecimal discountAmount;
    private BigDecimal fineAmount;

    @NotNull(message = "Payment date required")
    private LocalDate paymentDate;

    @NotNull(message = "Payment mode required")
    private PaymentMode paymentMode;

    // Cheque details
    private String chequeNumber;
    private LocalDate chequeDate;
    private String bankName;

    // Online payment
    private String transactionId;

    // Monthly fee
    private String feeMonth;          // "April 2024"
    private Integer feeYear;

    // Partial payment
    private Boolean isPartialPayment = false;
    private UUID previousCollectionId;

    // Concession
    private String concessionReason;

    private String remarks;
}