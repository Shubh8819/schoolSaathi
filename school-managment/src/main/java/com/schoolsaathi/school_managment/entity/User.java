package com.schoolsaathi.school_managment.entity;


import com.schoolsaathi.school_managment.entity.baseentity.BaseEntity;
import com.schoolsaathi.school_managment.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"school_id", "email"}
                )
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id",
            insertable = false,
            updatable = false)
    private School school;

    // Basic Info
    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    private String phone;

    @Column(nullable = false)
    private String password;

    // Role
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    // Teacher Specific
    private String employeeId;
    private String designation;
    private LocalDate joiningDate;

    // Status
    @Column(name = "is_active")
    private Boolean isActive = true;

    private LocalDateTime lastLoginAt;
}