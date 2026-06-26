package com.schoolsaathi.school_managment.entity;


import com.schoolsaathi.school_managment.entity.baseentity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "classes",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"school_id", "name"}
                )
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClassRoom extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id",
            insertable = false,
            updatable = false)
    private School school;

    @Column(nullable = false)
    private String name;            // "Class 1", "LKG"

    private Integer numericLevel;   // Sorting ke liye

    private String description;

    // Relationships
    @OneToMany(mappedBy = "classRoom",
            cascade = CascadeType.ALL)
    private List<Section> sections;

    @OneToMany(mappedBy = "classRoom",
            cascade = CascadeType.ALL)
    private List<Student> students;
}