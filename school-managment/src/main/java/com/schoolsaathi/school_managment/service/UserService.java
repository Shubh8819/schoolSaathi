package com.schoolsaathi.school_managment.service;

import com.schoolsaathi.school_managment.dto.request.UserCreateDto;
import com.schoolsaathi.school_managment.dto.request.UserUpdateDto;
import com.schoolsaathi.school_managment.dto.response.UserResponseDto;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserResponseDto createUser(UserCreateDto dto, UUID schoolId);

    UserResponseDto updateUser(UUID userId, UserUpdateDto dto);

    UserResponseDto getUserById(UUID userId);

    List<UserResponseDto> getAllUsers(UUID schoolId);

    List<UserResponseDto> searchUsers(UUID schoolId, String keyword);

    List<UserResponseDto> getUsersByDepartment(UUID departmentId);

    List<UserResponseDto> getUsersByDesignation(UUID designationId);

    void deleteUser(UUID userId);

    UserResponseDto toggleUserStatus(UUID userId);
}
