package com.schoolsaathi.school_managment.dto.response;



import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassRoomResponseDto {

    private UUID id;
    private String name;
    private Integer numericLevel;
    private Integer totalSections;    // Computed
    private Integer totalStudents;    // Computed
    private List<SectionResponseDto> sections;
}