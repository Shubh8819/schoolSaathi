package com.schoolsaathi.school_managment.entity;

import com.schoolsaathi.school_managment.entity.baseentity.BaseEntity;
import com.schoolsaathi.school_managment.enums.Authority;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "designations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Designation extends BaseEntity {

    @Column(nullable = false)
    private String title;

    @Column(length = 50)
    private String code;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @ElementCollection(targetClass = Authority.class, fetch = FetchType.EAGER)
    @CollectionTable(
            name = "designation_authorities",
            joinColumns = @JoinColumn(name = "designation_id")
    )
    @Column(name = "authority")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Set<Authority> authorities = new HashSet<>();
}
