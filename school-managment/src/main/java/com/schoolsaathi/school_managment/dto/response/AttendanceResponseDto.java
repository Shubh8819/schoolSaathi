package com.schoolsaathi.school_managment.dto.response;


import com.schoolsaathi.school_managment.enums.AttendanceStatus;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceResponseDto {

    private UUID id;
    private UUID studentId;
    private String studentName;
    private String admissionNumber;
    private LocalDate date;
    private AttendanceStatus status;
    private String reason;
    private String markedByName;
}