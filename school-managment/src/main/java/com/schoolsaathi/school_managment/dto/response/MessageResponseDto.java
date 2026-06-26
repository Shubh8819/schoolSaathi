package com.schoolsaathi.school_managment.dto.response;


import com.schoolsaathi.school_managment.enums.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponseDto {

    private UUID id;
    private String title;
    private String content;
    private TargetType targetType;
    private String targetName;        // Class/Section name
    private MessageChannel channel;
    private MessageStatus status;
    private Integer totalRecipients;
    private Integer sentCount;
    private Integer failedCount;
    private String sentByName;
    private LocalDateTime sentAt;
    private LocalDateTime createdAt;
}