package com.schoolsaathi.school_managment.entity;


import com.schoolsaathi.school_managment.entity.baseentity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "sections",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"school_id", "class_id", "name"}
                )
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Section extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id",
            insertable = false,
            updatable = false)
    private School school;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id")
    private ClassRoom classRoom;

    @Column(nullable = false)
    private String name;        // A, B, C

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_teacher_id")
    private User classTeacher;

    private Integer capacity;

    // Relationships
    @OneToMany(mappedBy = "section",
            cascade = CascadeType.ALL)
    private List<Student> students;
}