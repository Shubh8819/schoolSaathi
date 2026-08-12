package com.schoolsaathi.school_managment.service;

import com.schoolsaathi.school_managment.dto.request.StudentRegistrationDto;
import com.schoolsaathi.school_managment.dto.response.StudentResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface StudentService {

    StudentResponseDto registerStudent(StudentRegistrationDto dto, UUID schoolId, String createdBy,
                                       MultipartFile photo, MultipartFile birthCertificate,
                                       MultipartFile transferCertificate, MultipartFile aadhaarCard);

    StudentResponseDto getStudentById(UUID studentId, UUID schoolId);

    Page<StudentResponseDto> getAllStudents(UUID schoolId, Pageable pageable);

    Page<StudentResponseDto> searchStudents(UUID schoolId, String keyword, Pageable pageable);

    void deleteStudent(UUID studentId, UUID schoolId, String updatedBy);

    long getTotalStudentCount(UUID schoolId);
}

