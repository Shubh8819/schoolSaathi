package com.schoolsaathi.school_managment.mapper;

import com.schoolsaathi.school_managment.dto.request.StudentRegistrationDto;
import com.schoolsaathi.school_managment.entity.AcademicYear;
import com.schoolsaathi.school_managment.entity.ClassRoom;
import com.schoolsaathi.school_managment.entity.Section;
import com.schoolsaathi.school_managment.entity.Student;
import com.schoolsaathi.school_managment.enums.AdmissionCategory;
import com.schoolsaathi.school_managment.enums.StudentStatus;
import org.springframework.stereotype.Component;

/**
 * Maps StudentRegistrationDto -> Student entity.
 *
 * classId/sectionId/academicYearId/siblingStudentId are UUIDs in the DTO — this mapper
 * does NOT hit the DB to resolve them. Resolve ClassRoom/Section/AcademicYear/sibling
 * Student in the Service layer and pass the resolved entities in here, e.g.:
 *
 *   ClassRoom classRoom = classRoomRepository.findByIdAndSchoolId(dto.getClassId(), schoolId)
 *           .orElseThrow(() -> new ResourceNotFoundException("Class not found"));
 *   Student student = studentMapper.toEntity(dto, classRoom, section, academicYear, sibling);
 */
@Component
public class StudentMapper {

    public Student toEntity(StudentRegistrationDto dto, ClassRoom classRoom, Section section,
                             AcademicYear academicYear, Student sibling) {
        return Student.builder()
                .admissionNumber(dto.getAdmissionNumber())
                .name(dto.getName())
                .dob(dto.getDob())
                .gender(dto.getGender())
                .bloodGroup(dto.getBloodGroup())
                .photoUrl(dto.getPhotoUrl())
                .religion(dto.getReligion())
                .house(dto.getHouse())
                .aadharNumber(dto.getAadhaarNumber())
                .aadharCardUrl(dto.getAadhaarCardUrl())
                .category(parseCategory(dto.getCategory()))
                .classRoom(classRoom)
                .academicYear(academicYear)
                .rollNumber(dto.getRollNumber())
                .address(dto.getAddress())
                .addressLine2(dto.getAddressLine2())
                .city(dto.getCity())
                .state(dto.getState())
                .pincode(dto.getPincode())
                .country(dto.getCountry() != null ? dto.getCountry() : "India")
                .previousSchool(dto.getPreviousSchool())
                .admissionType(dto.getAdmissionType())
                .board(dto.getBoard())
                .transferCertificate(dto.getTransferCertificateUrl())
                .birthCertificate(dto.getBirthCertificateUrl())
                .medicalCondition(dto.getMedicalCondition())
                .hasSibling(Boolean.TRUE.equals(dto.getHasSibling()))
                .sibling(sibling)
                .admissionDate(dto.getAdmissionDate())
                .status(StudentStatus.ACTIVE)
                .build();
    }

    /** Applies DTO changes onto an existing managed entity (update flow) — relations passed in resolved. */
    public void updateEntity(Student student, StudentRegistrationDto dto, ClassRoom classRoom, Section section,
                              AcademicYear academicYear, Student sibling) {
        student.setName(dto.getName());
        student.setDob(dto.getDob());
        student.setGender(dto.getGender());
        student.setBloodGroup(dto.getBloodGroup());
        student.setPhotoUrl(dto.getPhotoUrl());
        student.setReligion(dto.getReligion());
        student.setHouse(dto.getHouse());
        student.setAadharNumber(dto.getAadhaarNumber());
        student.setAadharCardUrl(dto.getAadhaarCardUrl());
        student.setCategory(parseCategory(dto.getCategory()));
        student.setClassRoom(classRoom);


        student.setAcademicYear(academicYear);
        student.setRollNumber(dto.getRollNumber());
        student.setAddress(dto.getAddress());
        student.setAddressLine2(dto.getAddressLine2());
        student.setCity(dto.getCity());
        student.setState(dto.getState());
        student.setPincode(dto.getPincode());
        student.setCountry(dto.getCountry() != null ? dto.getCountry() : "India");
        student.setPreviousSchool(dto.getPreviousSchool());
        student.setAdmissionType(dto.getAdmissionType());
        student.setBoard(dto.getBoard());
        student.setTransferCertificate(dto.getTransferCertificateUrl());
        student.setBirthCertificate(dto.getBirthCertificateUrl());
        student.setMedicalCondition(dto.getMedicalCondition());
        student.setHasSibling(Boolean.TRUE.equals(dto.getHasSibling()));
        student.setSibling(sibling);
        student.setAdmissionDate(dto.getAdmissionDate());
    }

    private AdmissionCategory parseCategory(String category) {
        if (category == null || category.isBlank()) {
            return null;
        }
        try {
            return AdmissionCategory.valueOf(category.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid category: " + category
                    + ". Expected one of GENERAL, OBC, SC, ST, EWS, OTHER");
        }
    }
}
