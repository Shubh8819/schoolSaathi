package com.schoolsaathi.school_managment.dto.response;


import com.schoolsaathi.school_managment.enums.Relation;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParentResponseDto {

    private UUID id;
    private String name;
    private String phone;
    private String alternatePhone;
    private String email;
    private Relation relation;
    private String occupation;
    private String whatsappNumber;
    private Boolean isWhatsappActive;
    private Boolean isPrimary;
}