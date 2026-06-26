package com.schoolsaathi.school_managment.dto.request;


import com.schoolsaathi.school_managment.enums.MessageChannel;
import com.schoolsaathi.school_managment.enums.TargetType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {

    private String title;

    @NotBlank(message = "Message content required")
    private String content;

    @NotNull(message = "Target type required")
    private TargetType targetType;
    // ALL, CLASS, SECTION, INDIVIDUAL

    private UUID targetClassId;
    private UUID targetSectionId;
    private List<UUID> targetStudentIds;

    @NotNull(message = "Channel required")
    private MessageChannel channel;
    // SMS, WHATSAPP, BOTH
}