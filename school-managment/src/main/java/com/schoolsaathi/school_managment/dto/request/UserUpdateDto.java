package com.schoolsaathi.school_managment.dto.request;



import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateDto {

    private String name;
    private String phone;
    private String employeeId;
    private String designation;
    private LocalDate joiningDate;
    private Boolean isActive;
}