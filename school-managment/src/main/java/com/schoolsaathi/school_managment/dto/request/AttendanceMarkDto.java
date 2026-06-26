package com.schoolsaathi.school_managment.dto.request;


import com.schoolsaathi.school_managment.enums.AttendanceStatus;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceMarkDto {

    @NotNull(message = "Class required")
    private UUID classId;

    @NotNull(message = "Section required")
    private UUID sectionId;

    @NotNull(message = "Academic year required")
    private UUID academicYearId;

    @NotNull(message = "Date required")
    private LocalDate date;

    // Ek saath poori class ki attendance
    @NotEmpty(message = "Attendance list required")
    private List<StudentAttendanceDto> attendances;
}