package com.schoolsaathi.school_managment.service.serviceimpl;

import com.schoolsaathi.school_managment.dto.request.DepartmentDto;
import com.schoolsaathi.school_managment.dto.request.DesignationDto;
import com.schoolsaathi.school_managment.entity.Department;
import com.schoolsaathi.school_managment.entity.Designation;
import com.schoolsaathi.school_managment.mapper.DepartmentMapper;
import com.schoolsaathi.school_managment.repository.DepartmentRepository;
import com.schoolsaathi.school_managment.repository.DesignationRepository;
import com.schoolsaathi.school_managment.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final DesignationRepository designationRepository;
    private final DepartmentMapper departmentMapper;

    // ─────────────────────────────────────
    // Department Operations
    // ─────────────────────────────────────

    @Override
    public DepartmentDto createDepartment(DepartmentDto dto, UUID schoolId) {
        log.info("Creating new department: {} for schoolId: {}", dto.getName(), schoolId);

        if (schoolId != null && departmentRepository.existsByNameAndSchoolIdAndIsDeletedFalse(dto.getName(), schoolId)) {
            throw new IllegalArgumentException("Department with name '" + dto.getName() + "' already exists.");
        }

        Department department = departmentMapper.toDepartmentEntity(dto);
        department.setSchoolId(schoolId);

        Department saved = departmentRepository.save(department);
        return departmentMapper.toDepartmentDto(saved);
    }

    @Override
    public DepartmentDto updateDepartment(UUID departmentId, DepartmentDto dto) {
        log.info("Updating department ID: {}", departmentId);

        Department department = departmentRepository.findByIdAndIsDeletedFalse(departmentId)
                .orElseThrow(() -> new IllegalArgumentException("Department not found with ID: " + departmentId));

        department.setName(dto.getName());
        department.setCode(dto.getCode());
        department.setDescription(dto.getDescription());
        if (dto.getIsActive() != null) {
            department.setIsActive(dto.getIsActive());
        }

        Department updated = departmentRepository.save(department);
        return departmentMapper.toDepartmentDto(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public DepartmentDto getDepartmentById(UUID departmentId) {
        Department department = departmentRepository.findByIdAndIsDeletedFalse(departmentId)
                .orElseThrow(() -> new IllegalArgumentException("Department not found with ID: " + departmentId));

        return departmentMapper.toDepartmentDto(department);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentDto> getAllDepartments(UUID schoolId) {
        List<Department> departments;
        if (schoolId != null) {
            departments = departmentRepository.findAllBySchoolIdAndIsDeletedFalse(schoolId);
        } else {
            departments = departmentRepository.findAllByIsDeletedFalse();
        }

        return departments.stream()
                .map(departmentMapper::toDepartmentDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentDto> searchDepartments(UUID schoolId, String keyword) {
        return departmentRepository.searchDepartments(schoolId, keyword).stream()
                .map(departmentMapper::toDepartmentDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteDepartment(UUID departmentId) {
        log.info("Soft deleting department ID: {}", departmentId);

        Department department = departmentRepository.findByIdAndIsDeletedFalse(departmentId)
                .orElseThrow(() -> new IllegalArgumentException("Department not found with ID: " + departmentId));

        department.setIsDeleted(true);
        if (department.getDesignations() != null) {
            department.getDesignations().forEach(d -> d.setIsDeleted(true));
        }
        departmentRepository.save(department);
    }

    @Override
    public DepartmentDto toggleDepartmentStatus(UUID departmentId) {
        Department department = departmentRepository.findByIdAndIsDeletedFalse(departmentId)
                .orElseThrow(() -> new IllegalArgumentException("Department not found with ID: " + departmentId));

        department.setIsActive(!Boolean.TRUE.equals(department.getIsActive()));
        Department updated = departmentRepository.save(department);
        return departmentMapper.toDepartmentDto(updated);
    }

    // ─────────────────────────────────────
    // Designation Operations
    // ─────────────────────────────────────

    @Override
    public DesignationDto createDesignation(DesignationDto dto, UUID schoolId) {
        log.info("Creating designation: {} under department ID: {}", dto.getTitle(), dto.getDepartmentId());

        Department department = departmentRepository.findByIdAndIsDeletedFalse(dto.getDepartmentId())
                .orElseThrow(() -> new IllegalArgumentException("Department not found with ID: " + dto.getDepartmentId()));

        if (designationRepository.existsByTitleAndDepartmentIdAndIsDeletedFalse(dto.getTitle(), dto.getDepartmentId())) {
            throw new IllegalArgumentException("Designation '" + dto.getTitle() + "' already exists in department '" + department.getName() + "'");
        }

        Designation designation = departmentMapper.toDesignationEntity(dto, department);
        designation.setSchoolId(schoolId != null ? schoolId : department.getSchoolId());

        Designation saved = designationRepository.save(designation);
        return departmentMapper.toDesignationDto(saved);
    }

    @Override
    public DesignationDto updateDesignation(UUID designationId, DesignationDto dto) {
        log.info("Updating designation ID: {}", designationId);

        Designation designation = designationRepository.findByIdAndIsDeletedFalse(designationId)
                .orElseThrow(() -> new IllegalArgumentException("Designation not found with ID: " + designationId));

        designation.setTitle(dto.getTitle());
        designation.setCode(dto.getCode());
        designation.setDescription(dto.getDescription());
        if (dto.getIsActive() != null) {
            designation.setIsActive(dto.getIsActive());
        }

        if (dto.getAuthorities() != null) {
            designation.setAuthorities(new HashSet<>(dto.getAuthorities()));
        }

        Designation updated = designationRepository.save(designation);
        return departmentMapper.toDesignationDto(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public DesignationDto getDesignationById(UUID designationId) {
        Designation designation = designationRepository.findByIdAndIsDeletedFalse(designationId)
                .orElseThrow(() -> new IllegalArgumentException("Designation not found with ID: " + designationId));

        return departmentMapper.toDesignationDto(designation);
    }

    @Override
    @Transactional(readOnly = true)
    public List<DesignationDto> getDesignationsByDepartment(UUID departmentId) {
        return designationRepository.findAllByDepartmentIdAndIsDeletedFalse(departmentId).stream()
                .map(departmentMapper::toDesignationDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteDesignation(UUID designationId) {
        log.info("Soft deleting designation ID: {}", designationId);

        Designation designation = designationRepository.findByIdAndIsDeletedFalse(designationId)
                .orElseThrow(() -> new IllegalArgumentException("Designation not found with ID: " + designationId));

        designation.setIsDeleted(true);
        designationRepository.save(designation);
    }

    @Override
    public DesignationDto toggleDesignationStatus(UUID designationId) {
        Designation designation = designationRepository.findByIdAndIsDeletedFalse(designationId)
                .orElseThrow(() -> new IllegalArgumentException("Designation not found with ID: " + designationId));

        designation.setIsActive(!Boolean.TRUE.equals(designation.getIsActive()));
        Designation updated = designationRepository.save(designation);
        return departmentMapper.toDesignationDto(updated);
    }
}
