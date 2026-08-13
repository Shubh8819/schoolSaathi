package com.schoolsaathi.school_managment.entity;

import com.schoolsaathi.school_managment.entity.baseentity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "fee_template",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {
                                "school_id",
                                "class_id",
                                "fee_head"
                        }
                )
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeeTemplate extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id",insertable = false,updatable = false)
    private School school;

    @Id
    private UUID id;

    private String name;

    private Boolean isDefault;

    @OneToMany(mappedBy = "template")
    private List<FeeStructure> feeHeads;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id", nullable = false)
    private ClassRoom classRoom;

    @Override
    public String toString() {
        return "FeeTemplate{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isDefault=" + isDefault +
                ", feeHeads=" + feeHeads +

                '}';
    }
}
