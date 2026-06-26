package com.schoolsaathi.school_managment.entity;



import com.schoolsaathi.school_managment.entity.baseentity.BaseEntity;
import com.schoolsaathi.school_managment.enums.MessageChannel;
import com.schoolsaathi.school_managment.enums.MessageStatus;
import com.schoolsaathi.school_managment.enums.TargetType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id",
            insertable = false,
            updatable = false)
    private School school;

    // Message Details
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    // Target
    @Enumerated(EnumType.STRING)
    private TargetType targetType;
    // ALL, CLASS, SECTION, INDIVIDUAL

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_class_id")
    private ClassRoom targetClass;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "target_section_id")
    private Section targetSection;

    // Channel
    @Enumerated(EnumType.STRING)
    private MessageChannel channel;
    // SMS, WHATSAPP, BOTH

    // Status
    @Enumerated(EnumType.STRING)
    private MessageStatus status = MessageStatus.PENDING;

    private Integer totalRecipients = 0;
    private Integer sentCount = 0;
    private Integer failedCount = 0;

    // Sent By
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sent_by")
    private User sentBy;

    private LocalDateTime sentAt;
}