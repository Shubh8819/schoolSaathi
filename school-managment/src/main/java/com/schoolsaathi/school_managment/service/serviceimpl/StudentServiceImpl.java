package com.schoolsaathi.school_managment.service.serviceimpl;

import com.schoolsaathi.school_managment.dto.request.StudentRegistrationDto;
import com.schoolsaathi.school_managment.entity.AcademicYear;
import com.schoolsaathi.school_managment.entity.ClassRoom;
import com.schoolsaathi.school_managment.entity.Section;
import com.schoolsaathi.school_managment.entity.Student;
import com.schoolsaathi.school_managment.exception.DuplicateResourceException;
import com.schoolsaathi.school_managment.exception.ResourceNotFoundException;
import com.schoolsaathi.school_managment.mapper.StudentMapper;
// TODO: adjust these imports/method names to match your actual repositories
import com.schoolsaathi.school_managment.repository.AcademicYearRepository;
import com.schoolsaathi.school_managment.repository.ClassRoomRepository;
import com.schoolsaathi.school_managment.repository.SectionRepository;
import com.schoolsaathi.school_managment.repository.StudentRepository;
import com.schoolsaathi.school_managment.service.FileStorageService;
import com.schoolsaathi.school_managment.service.StudentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import com.schoolsaathi.school_managment.dto.response.StudentResponseDto;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final ClassRoomRepository classRoomRepository;
    private final SectionRepository sectionRepository;
    private final AcademicYearRepository academicYearRepository;
    private final StudentMapper studentMapper;
    private final FileStorageService fileStorageService;

    @Override
    @Transactional
    public StudentResponseDto registerStudent(StudentRegistrationDto dto, UUID schoolId, String createdBy,
                                               MultipartFile photo, MultipartFile birthCertificate,
                                               MultipartFile transferCertificate, MultipartFile aadhaarCard) {

        if (dto.getAdmissionNumber() != null
                && studentRepository.existsByAdmissionNumberAndSchoolIdAndIsDeletedFalse(dto.getAdmissionNumber(), schoolId)) {
            throw new DuplicateResourceException(
                    "Admission number '" + dto.getAdmissionNumber() + "' already exists for this school");
        }

        ClassRoom classRoom = classRoomRepository.findByIdAndSchoolIdAndIsDeletedFalse(dto.getClassId(),schoolId)
                .orElseThrow(() -> new ResourceNotFoundException("Class not found"));
        Section section = sectionRepository.findByIdAndSchoolIdAndIsDeletedFalse(dto.getSectionId(), schoolId)
                .orElseThrow(() -> new ResourceNotFoundException("Section not found"));
        AcademicYear academicYear = academicYearRepository.findByIdAndSchoolIdAndIsDeletedFalse(dto.getAcademicYearId(), schoolId)
                .orElseThrow(() -> new ResourceNotFoundException("Academic year not found"));

        Student sibling = null;
        if (Boolean.TRUE.equals(dto.getHasSibling()) && dto.getSiblingStudentId() != null) {
            sibling = studentRepository.findByIdAndSchoolIdAndIsDeletedFalse(dto.getSiblingStudentId(), schoolId)
                    .orElseThrow(() -> new ResourceNotFoundException("Sibling student not found"));
        }

        if (dto.getAdmissionNumber() == null || dto.getAdmissionNumber().isBlank()) {
            dto.setAdmissionNumber(generateAdmissionNumber(schoolId));
        }

        Student student = studentMapper.toEntity(dto, classRoom, section, academicYear, sibling);
        student.setSchoolId(schoolId);
        student.setCreatedBy(createdBy);

        Student saved = studentRepository.save(student);

        String refId = saved.getAdmissionNumber();
        saved.setPhotoUrl(fileStorageService.store(photo, schoolId, refId, "photo"));
        saved.setBirthCertificate(fileStorageService.store(birthCertificate, schoolId, refId, "birth-certificate"));
        saved.setTransferCertificate(fileStorageService.store(transferCertificate, schoolId, refId, "transfer-certificate"));
        saved.setAadharCardUrl(fileStorageService.store(aadhaarCard, schoolId, refId, "aadhaar-card"));

        // NOTE: dto.getParents() is NOT persisted here — ParentDto/ParentRepository
        // weren't available when this service was written. Wire that separately:
        // for each ParentDto -> map to Parent, set parent.setStudent(saved), save via ParentRepository
        // (or cascade it by adding the mapped Parent list to saved.setParents(...) before this save,
        // since Student -> Parent is CascadeType.ALL).
        if (dto.getParents() == null || dto.getParents().isEmpty()) {
            log.warn("Student {} registered without parent records — parent mapping not wired yet", saved.getAdmissionNumber());
        }

        return mapToResponse(studentRepository.save(saved));
    }

    @Override
    public StudentResponseDto getStudentById(UUID studentId, UUID schoolId) {
        return mapToResponse(findByIdOrThrow(studentId, schoolId));
    }

    @Override
    public Page<StudentResponseDto> getAllStudents(UUID schoolId, Pageable pageable) {
        return studentRepository.findAllBySchoolIdAndIsDeletedFalse(schoolId, pageable).map(this::mapToResponse);
    }

    @Override
    public Page<StudentResponseDto> searchStudents(UUID schoolId, String keyword, Pageable pageable) {
        if (keyword == null || keyword.isBlank()) {
            return getAllStudents(schoolId, pageable);
        }
        return studentRepository.searchBySchoolId(schoolId, keyword.trim(), pageable).map(this::mapToResponse);
    }

    @Override
    @Transactional
    public void deleteStudent(UUID studentId, UUID schoolId, String updatedBy) {
        Student student = findByIdOrThrow(studentId, schoolId);
        student.setIsDeleted(true);
        student.setUpdatedBy(updatedBy);
        studentRepository.save(student);
    }

    @Override
    public long getTotalStudentCount(UUID schoolId) {
        return studentRepository.countBySchoolIdAndIsDeletedFalse(schoolId);
    }

    // ---------- helpers ----------

    private Student findByIdOrThrow(UUID studentId, UUID schoolId) {
        return studentRepository.findByIdAndSchoolIdAndIsDeletedFalse(studentId, schoolId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));
    }

    private String generateAdmissionNumber(UUID schoolId) {
        long count = studentRepository.countBySchoolIdAndIsDeletedFalse(schoolId) + 1;
        return java.time.Year.now().getValue() + String.format("%04d", count);
    }

    private StudentResponseDto mapToResponse(Student s) {
        return StudentResponseDto.builder()
                .id(s.getId())
                .admissionNumber(s.getAdmissionNumber())
                .name(s.getName())
                .className(s.getClassRoom() != null ? s.getClassRoom().getName() : null)
                //.sectionName(s.getSection() != null ? s.getSection().getName() : null)
                .rollNumber(s.getRollNumber())
                .photoUrl(s.getPhotoUrl())
                .status(s.getStatus())
                .admissionDate(s.getAdmissionDate())
                .build();
    }
}
