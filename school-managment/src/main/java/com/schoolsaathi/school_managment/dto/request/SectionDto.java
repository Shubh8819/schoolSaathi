package com.schoolsaathi.school_managment.dto.request;


import jakarta.validation.constraints.*;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SectionDto {

    @NotNull(message = "Class required")
    private UUID classId;

    @NotBlank(message = "Section name required")
    private String name;              // A, B, C

    private UUID classTeacherId;
    private Integer capacity;
}