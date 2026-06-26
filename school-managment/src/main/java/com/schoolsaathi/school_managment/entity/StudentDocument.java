package com.schoolsaathi.school_managment.entity;

import com.schoolsaathi.school_managment.entity.baseentity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "student_documents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentDocument extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    private String documentType;
    // BIRTH_CERTIFICATE, AADHAR,
    // TRANSFER_CERTIFICATE, PHOTO,
    // CASTE_CERTIFICATE, INCOME_CERTIFICATE

    private String documentUrl;      // S3/local path
    private String fileName;
    private Boolean isVerified = false;
    private LocalDate uploadedDate;
    private String remarks;
}