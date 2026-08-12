package com.schoolsaathi.school_managment.controller.web;

import com.schoolsaathi.school_managment.dto.request.DepartmentDto;
import com.schoolsaathi.school_managment.dto.request.DesignationDto;
import com.schoolsaathi.school_managment.dto.request.UserCreateDto;
import com.schoolsaathi.school_managment.dto.request.UserUpdateDto;
import com.schoolsaathi.school_managment.dto.response.UserResponseDto;
import com.schoolsaathi.school_managment.enums.UserRole;
import com.schoolsaathi.school_managment.security.CustomUserDetails;
import com.schoolsaathi.school_managment.service.DepartmentService;
import com.schoolsaathi.school_managment.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Slf4j
@Controller
@RequestMapping("/web/users")
@RequiredArgsConstructor
public class UserWebController {

    private final UserService userService;
    private final DepartmentService departmentService;

    private UUID getSchoolId(CustomUserDetails userDetails) {
        return userDetails != null ? userDetails.getSchoolId() : null;
    }

    // LIST USERS
    @GetMapping
    public String listUsers(
            @RequestParam(required = false) String keyword,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model) {

        UUID schoolId = getSchoolId(userDetails);
        List<UserResponseDto> users;

        if (keyword != null && !keyword.trim().isEmpty()) {
            users = userService.searchUsers(schoolId, keyword.trim());
            model.addAttribute("keyword", keyword.trim());
        } else {
            users = userService.getAllUsers(schoolId);
        }

        model.addAttribute("users", users);
        model.addAttribute("totalUsers", users.size());
        model.addAttribute("activePage", "users");

        return "user/list";
    }

    // USER DETAIL PROFILE
    @GetMapping("/{id}")
    public String userDetail(
            @PathVariable UUID id,
            Model model) {

        UserResponseDto user = userService.getUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("activePage", "users");

        return "user/detail";
    }

    // ADD USER FORM
    @GetMapping("/add")
    public String showAddForm(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model) {

        UUID schoolId = getSchoolId(userDetails);
        List<DepartmentDto> departments = departmentService.getAllDepartments(schoolId);

        model.addAttribute("userDto", new UserCreateDto());
        model.addAttribute("departments", departments);
        model.addAttribute("roles", UserRole.values());
        model.addAttribute("activePage", "users");

        return "user/add";
    }

    // SUBMIT ADD USER
    @PostMapping("/add")
    public String addUser(
            @Valid @ModelAttribute("userDto") UserCreateDto dto,
            BindingResult result,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            RedirectAttributes redirectAttributes,
            Model model) {

        UUID schoolId = getSchoolId(userDetails);

        if (result.hasErrors()) {
            model.addAttribute("departments", departmentService.getAllDepartments(schoolId));
            model.addAttribute("roles", UserRole.values());
            model.addAttribute("activePage", "users");
            return "user/add";
        }

        try {
            UserResponseDto created = userService.createUser(dto, schoolId);
            redirectAttributes.addFlashAttribute("successMessage", "User '" + created.getName() + "' created successfully!");
            return "redirect:/web/users";
        } catch (Exception e) {
            log.error("Error creating user: {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("departments", departmentService.getAllDepartments(schoolId));
            model.addAttribute("roles", UserRole.values());
            model.addAttribute("activePage", "users");
            return "user/add";
        }
    }

    // EDIT USER FORM
    @GetMapping("/{id}/edit")
    public String showEditForm(
            @PathVariable UUID id,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model) {

        UUID schoolId = getSchoolId(userDetails);
        UserResponseDto user = userService.getUserById(id);

        UserUpdateDto updateDto = UserUpdateDto.builder()
                .name(user.getName())
                .phone(user.getPhone())
                .role(user.getRole())
                .employeeId(user.getEmployeeId())
                .designation(user.getDesignation())
                .joiningDate(user.getJoiningDate())
                .isActive(user.getIsActive())
                .departmentId(user.getDepartmentId())
                .designationId(user.getDesignationId())
                .build();

        List<DepartmentDto> departments = departmentService.getAllDepartments(schoolId);
        List<DesignationDto> designations = user.getDepartmentId() != null
                ? departmentService.getDesignationsByDepartment(user.getDepartmentId())
                : Collections.emptyList();

        model.addAttribute("userDto", updateDto);
        model.addAttribute("userId", id);
        model.addAttribute("departments", departments);
        model.addAttribute("designations", designations);
        model.addAttribute("roles", UserRole.values());
        model.addAttribute("activePage", "users");

        return "user/edit";
    }

    // SUBMIT EDIT USER
    @PostMapping("/{id}/edit")
    public String updateUser(
            @PathVariable UUID id,
            @ModelAttribute("userDto") UserUpdateDto dto,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            RedirectAttributes redirectAttributes,
            Model model) {

        UUID schoolId = getSchoolId(userDetails);

        try {
            userService.updateUser(id, dto);
            redirectAttributes.addFlashAttribute("successMessage", "User updated successfully!");
            return "redirect:/web/users/" + id;
        } catch (Exception e) {
            log.error("Error updating user: {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("userId", id);
            model.addAttribute("departments", departmentService.getAllDepartments(schoolId));
            model.addAttribute("roles", UserRole.values());
            model.addAttribute("activePage", "users");
            return "user/edit";
        }
    }

    // TOGGLE STATUS
    @PostMapping("/{id}/toggle")
    public String toggleUserStatus(
            @PathVariable UUID id,
            RedirectAttributes redirectAttributes) {

        UserResponseDto updated = userService.toggleUserStatus(id);
        String status = Boolean.TRUE.equals(updated.getIsActive()) ? "activated" : "deactivated";
        redirectAttributes.addFlashAttribute("successMessage", "User " + status + "!");
        return "redirect:/web/users";
    }

    // DELETE USER
    @PostMapping("/{id}/delete")
    public String deleteUser(
            @PathVariable UUID id,
            RedirectAttributes redirectAttributes) {

        userService.deleteUser(id);
        redirectAttributes.addFlashAttribute("successMessage", "User deleted successfully!");
        return "redirect:/web/users";
    }
}
