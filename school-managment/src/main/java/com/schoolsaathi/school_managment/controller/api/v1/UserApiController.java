package com.schoolsaathi.school_managment.controller.api.v1;

import com.schoolsaathi.school_managment.dto.common.ApiResponse;
import com.schoolsaathi.school_managment.dto.request.UserCreateDto;
import com.schoolsaathi.school_managment.dto.request.UserUpdateDto;
import com.schoolsaathi.school_managment.dto.response.UserResponseDto;
import com.schoolsaathi.school_managment.security.CustomUserDetails;
import com.schoolsaathi.school_managment.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    // GET ALL USERS
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponseDto>>> getAllUsers(
            @RequestParam(required = false) String keyword,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UUID schoolId = userDetails != null ? userDetails.getSchoolId() : null;
        List<UserResponseDto> users = (keyword != null && !keyword.isEmpty())
                ? userService.searchUsers(schoolId, keyword)
                : userService.getAllUsers(schoolId);

        return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", users));
    }

    // GET USER BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUserById(@PathVariable UUID id) {
        UserResponseDto user = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success("User retrieved successfully", user));
    }

    // CREATE USER
    @PostMapping
    public ResponseEntity<ApiResponse<UserResponseDto>> createUser(
            @Valid @RequestBody UserCreateDto dto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UUID schoolId = userDetails != null ? userDetails.getSchoolId() : null;
        UserResponseDto created = userService.createUser(dto, schoolId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("User created successfully", created));
    }

    // UPDATE USER
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponseDto>> updateUser(
            @PathVariable UUID id,
            @RequestBody UserUpdateDto dto) {

        UserResponseDto updated = userService.updateUser(id, dto);
        return ResponseEntity.ok(ApiResponse.success("User updated successfully", updated));
    }

    // DELETE USER
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully", null));
    }

    // TOGGLE USER STATUS
    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<ApiResponse<UserResponseDto>> toggleUserStatus(@PathVariable UUID id) {
        UserResponseDto updated = userService.toggleUserStatus(id);
        return ResponseEntity.ok(ApiResponse.success("User status updated", updated));
    }
}
