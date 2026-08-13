package com.schoolsaathi.school_managment.entity;


import com.schoolsaathi.school_managment.entity.baseentity.BaseEntity;
import com.schoolsaathi.school_managment.enums.PaymentMode;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "fee_collections",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"school_id", "receipt_number"}
                )
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeeCollection extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id",insertable = false,updatable = false)
    private School school;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fee_structure_id")
    private FeeStructure feeStructure;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "academic_year_id")
    private AcademicYear academicYear;

    // Payment Details
    @Column(name = "amount_paid",nullable = false,precision = 10, scale = 2)
    private BigDecimal amountPaid;

    @Column(name = "discount_amount",precision = 10, scale = 2)
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(name = "fine_amount",precision = 10, scale = 2)
    private BigDecimal fineAmount = BigDecimal.ZERO;

    @Column(name = "total_amount",nullable = false,precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "payment_date", nullable = false)
    private LocalDate paymentDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_mode", nullable = false)
    private PaymentMode paymentMode;
    // CASH, CHEQUE, ONLINE, UPI, DD

    // Receipt
    @Column(name = "receipt_number", nullable = false)
    private String receiptNumber;   // 2024-25/001

    // Cheque Details
    private String chequeNumber;
    private LocalDate chequeDate;
    private String bankName;

    // Online Payment
    private String transactionId;
    private String paymentGateway;  // Razorpay

    // Monthly Fee Tracking
    private String feeMonth;        // "April 2024"
    private Integer feeYear;

    // Staff
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collected_by")
    private User collectedBy;

    private String remarks;
}