package com.schoolsaathi.school_managment.service.serviceimpl;

import com.schoolsaathi.school_managment.dto.request.UserCreateDto;
import com.schoolsaathi.school_managment.dto.request.UserUpdateDto;
import com.schoolsaathi.school_managment.dto.response.UserResponseDto;
import com.schoolsaathi.school_managment.entity.Department;
import com.schoolsaathi.school_managment.entity.Designation;
import com.schoolsaathi.school_managment.entity.User;
import com.schoolsaathi.school_managment.mapper.UserMapper;
import com.schoolsaathi.school_managment.repository.DepartmentRepository;
import com.schoolsaathi.school_managment.repository.DesignationRepository;
import com.schoolsaathi.school_managment.repository.UserRepository;
import com.schoolsaathi.school_managment.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final DesignationRepository designationRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDto createUser(UserCreateDto dto, UUID schoolId) {
        log.info("Creating user: {} with email: {} for schoolId: {}", dto.getName(), dto.getEmail(), schoolId);

        if (userRepository.existsByEmailAndIsDeletedFalse(dto.getEmail())) {
            throw new IllegalArgumentException("User with email '" + dto.getEmail() + "' already exists.");
        }

        Department department = null;
        if (dto.getDepartmentId() != null) {
            department = departmentRepository.findByIdAndIsDeletedFalse(dto.getDepartmentId())
                    .orElseThrow(() -> new IllegalArgumentException("Department not found with ID: " + dto.getDepartmentId()));
        }

        Designation designation = null;
        if (dto.getDesignationId() != null) {
            designation = designationRepository.findByIdAndIsDeletedFalse(dto.getDesignationId())
                    .orElseThrow(() -> new IllegalArgumentException("Designation not found with ID: " + dto.getDesignationId()));
        }

        User user = User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(dto.getRole())
                .employeeId(dto.getEmployeeId())
                .designation(designation != null ? designation.getTitle() : dto.getDesignation())
                .joiningDate(dto.getJoiningDate())
                .isActive(true)
                .department(department)
                .designationEntity(designation)
                .build();

        user.setSchoolId(schoolId);

        User saved = userRepository.save(user);
        return userMapper.toDto(saved);
    }

    @Override
    public UserResponseDto updateUser(UUID userId, UserUpdateDto dto) {
        log.info("Updating user ID: {}", userId);

        User user = userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        if (dto.getName() != null) user.setName(dto.getName());
        if (dto.getPhone() != null) user.setPhone(dto.getPhone());
        if (dto.getRole() != null) user.setRole(dto.getRole());
        if (dto.getEmployeeId() != null) user.setEmployeeId(dto.getEmployeeId());
        if (dto.getJoiningDate() != null) user.setJoiningDate(dto.getJoiningDate());
        if (dto.getIsActive() != null) user.setIsActive(dto.getIsActive());

        if (dto.getDepartmentId() != null) {
            Department department = departmentRepository.findByIdAndIsDeletedFalse(dto.getDepartmentId())
                    .orElseThrow(() -> new IllegalArgumentException("Department not found with ID: " + dto.getDepartmentId()));
            user.setDepartment(department);
        }

        if (dto.getDesignationId() != null) {
            Designation designation = designationRepository.findByIdAndIsDeletedFalse(dto.getDesignationId())
                    .orElseThrow(() -> new IllegalArgumentException("Designation not found with ID: " + dto.getDesignationId()));
            user.setDesignationEntity(designation);
            user.setDesignation(designation.getTitle());
        } else if (dto.getDesignation() != null) {
            user.setDesignation(dto.getDesignation());
        }

        User updated = userRepository.save(user);
        return userMapper.toDto(updated);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUserById(UUID userId) {
        User user = userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        return userMapper.toDto(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllUsers(UUID schoolId) {
        List<User> users;
        if (schoolId != null) {
            users = userRepository.findAllBySchoolIdAndIsDeletedFalse(schoolId);
        } else {
            users = userRepository.findAll();
        }

        return users.stream()
                .filter(u -> u.getIsDeleted() == null || !u.getIsDeleted())
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> searchUsers(UUID schoolId, String keyword) {
        return userRepository.searchUsers(schoolId, keyword).stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> getUsersByDepartment(UUID departmentId) {
        return userRepository.findAllByDepartmentIdAndIsDeletedFalse(departmentId).stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> getUsersByDesignation(UUID designationId) {
        return userRepository.findAllByDesignationEntityIdAndIsDeletedFalse(designationId).stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(UUID userId) {
        log.info("Soft deleting user ID: {}", userId);

        User user = userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        user.setIsDeleted(true);
        userRepository.save(user);
    }

    @Override
    public UserResponseDto toggleUserStatus(UUID userId) {
        User user = userRepository.findByIdAndIsDeletedFalse(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        user.setIsActive(!Boolean.TRUE.equals(user.getIsActive()));
        User updated = userRepository.save(user);
        return userMapper.toDto(updated);
    }
}
