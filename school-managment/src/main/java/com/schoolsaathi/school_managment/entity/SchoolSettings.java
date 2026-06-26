package com.schoolsaathi.school_managment.entity;

import com.schoolsaathi.school_managment.entity.baseentity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "school_settings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SchoolSettings extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "school_id",
            insertable = false,
            updatable = false)
    private School school;

    // Academic Settings
    private Integer maxClassCapacity;
    private Boolean allowSiblingDiscount;
    private Integer siblingDiscountPercent;
    private Boolean allowPartialPayment;
    private Integer lateFinePerDay;
    private Integer gracePeriodDays;

    // Attendance Settings
    private Integer minAttendancePercent; // 75%
    private Boolean sendAbsentSms;
    private Boolean sendAbsentWhatsapp;
    private String attendanceMarkingTime;  // "9:00 AM"

    // Notification Settings
    private Boolean feeDueReminder;
    private Integer reminderDaysBefore;   // 3 days pehle
    private Boolean birthdayWishes;
    // Auto birthday message parents ko

    // Receipt Settings
    private String receiptFooterText;
    private Boolean showSchoolSeal;
    private String receiptCopies;         // SINGLE, DUPLICATE
}