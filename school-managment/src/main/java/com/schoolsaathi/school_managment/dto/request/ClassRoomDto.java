package com.schoolsaathi.school_managment.dto.request;


import jakarta.validation.constraints.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassRoomDto {

    @NotBlank(message = "Class name required")
    private String name;              // "Class 1", "LKG"

    private Integer numericLevel;     // Sorting ke liye
    private String description;
}