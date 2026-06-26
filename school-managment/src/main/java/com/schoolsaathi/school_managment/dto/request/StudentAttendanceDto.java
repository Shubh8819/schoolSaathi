package com.schoolsaathi.school_managment.dto.request;


import com.schoolsaathi.school_managment.enums.AttendanceStatus;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentAttendanceDto {

    @NotNull(message = "Student required")
    private UUID studentId;

    @NotNull(message = "Status required")
    private AttendanceStatus status;

    private String reason;            // Absence reason
}