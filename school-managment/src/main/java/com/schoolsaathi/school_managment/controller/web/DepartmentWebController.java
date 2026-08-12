package com.schoolsaathi.school_managment.controller.web;

import com.schoolsaathi.school_managment.dto.request.DepartmentDto;
import com.schoolsaathi.school_managment.dto.request.DesignationDto;
import com.schoolsaathi.school_managment.enums.Authority;
import com.schoolsaathi.school_managment.security.CustomUserDetails;
import com.schoolsaathi.school_managment.service.DepartmentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Session;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Slf4j
@Controller
@RequestMapping("/web/departments")
@RequiredArgsConstructor
public class DepartmentWebController {

    private final DepartmentService departmentService;

    private UUID getSchoolId(CustomUserDetails userDetails) {
        return userDetails != null ? userDetails.getSchoolId() : null;
    }

    // ─────────────────────────────────────
    // LIST — All Departments
    // ─────────────────────────────────────
    @GetMapping
    public String listDepartments(
            @RequestParam(required = false) String keyword,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model) {

        UUID schoolId = getSchoolId(userDetails);
        List<DepartmentDto> departments;

        if (keyword != null && !keyword.trim().isEmpty()) {
            departments = departmentService.searchDepartments(schoolId, keyword.trim());
            model.addAttribute("keyword", keyword.trim());
        } else {
            departments = departmentService.getAllDepartments(schoolId);
        }

        model.addAttribute("departments", departments);
        model.addAttribute("totalDepartments", departments.size());
        model.addAttribute("activePage", "departments");

        return "department/list";
    }

    // ─────────────────────────────────────
    // DETAIL — View Department & Designations
    // ─────────────────────────────────────
    @GetMapping("/{id}")
    public String departmentDetail(
            @PathVariable UUID id,
            Model model) {

        DepartmentDto department = departmentService.getDepartmentById(id);
        model.addAttribute("department", department);
        model.addAttribute("allAuthorities", Authority.values());
        model.addAttribute("activePage", "departments");

        return "department/detail";
    }

    // ─────────────────────────────────────
    // ADD — Department Form & Submission
    // ─────────────────────────────────────
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("departmentDto", new DepartmentDto());
        model.addAttribute("activePage", "departments");
        return "department/add";
    }

    @PostMapping("/add")
    public String addDepartment(
            @Valid @ModelAttribute("departmentDto") DepartmentDto dto,
            BindingResult result,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("activePage", "departments");
            return "department/add";
        }

        try {

            UUID schoolId = getSchoolId(userDetails);


            dto.setCreatedBy(String.valueOf(userDetails.getUserId()));
            DepartmentDto saved = departmentService.createDepartment(dto, schoolId);
            redirectAttributes.addFlashAttribute("successMessage", "Department '" + saved.getName() + "' created successfully!");
            return "redirect:/web/departments";
        } catch (Exception e) {
            log.error("Error creating department: {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("activePage", "departments");
            return "department/add";
        }
    }

    // ─────────────────────────────────────
    // EDIT — Department Form & Submission
    // ─────────────────────────────────────
    @GetMapping("/{id}/edit")
    public String showEditForm(
            @PathVariable UUID id,
            Model model) {

        DepartmentDto department = departmentService.getDepartmentById(id);
        model.addAttribute("departmentDto", department);
        model.addAttribute("departmentId", id);
        model.addAttribute("activePage", "departments");
        return "department/edit";
    }

    @PostMapping("/{id}/edit")
    public String updateDepartment(
            @PathVariable UUID id,
            @Valid @ModelAttribute("departmentDto") DepartmentDto dto,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("departmentId", id);
            model.addAttribute("activePage", "departments");
            return "department/edit";
        }

        try {
            departmentService.updateDepartment(id, dto);
            redirectAttributes.addFlashAttribute("successMessage", "Department updated successfully!");
            return "redirect:/web/departments/" + id;
        } catch (Exception e) {
            log.error("Error updating department: {}", e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("departmentId", id);
            model.addAttribute("activePage", "departments");
            return "department/edit";
        }
    }

    // ─────────────────────────────────────
    // DELETE & TOGGLE STATUS — Department
    // ─────────────────────────────────────
    @PostMapping("/{id}/delete")
    public String deleteDepartment(
            @PathVariable UUID id,
            RedirectAttributes redirectAttributes) {

        departmentService.deleteDepartment(id);
        redirectAttributes.addFlashAttribute("successMessage", "Department deleted successfully!");
        return "redirect:/web/departments";
    }

    @PostMapping("/{id}/toggle")
    public String toggleDepartmentStatus(
            @PathVariable UUID id,
            RedirectAttributes redirectAttributes) {

        DepartmentDto updated = departmentService.toggleDepartmentStatus(id);
        String statusText = Boolean.TRUE.equals(updated.getIsActive()) ? "activated" : "deactivated";
        redirectAttributes.addFlashAttribute("successMessage", "Department " + statusText + "!");
        return "redirect:/web/departments";
    }

    // ─────────────────────────────────────
    // DESIGNATIONS — Add Form & Submission
    // ─────────────────────────────────────
    @GetMapping("/{departmentId}/designations/add")
    public String showAddDesignationForm(
            @PathVariable UUID departmentId,
            Model model) {

        DepartmentDto department = departmentService.getDepartmentById(departmentId);
        DesignationDto designationDto = DesignationDto.builder()
                .departmentId(departmentId)
                .departmentName(department.getName())
                .authorities(new HashSet<>())
                .build();

        Map<String, List<Authority>> authoritiesByCategory = new LinkedHashMap<>();
        for (Authority auth : Authority.values()) {
            authoritiesByCategory.computeIfAbsent(auth.getCategory(), k -> new ArrayList<>()).add(auth);
        }

        model.addAttribute("designationDto", designationDto);
        model.addAttribute("department", department);
        model.addAttribute("authoritiesByCategory", authoritiesByCategory);
        model.addAttribute("isEdit", false);
        model.addAttribute("activePage", "departments");

        return "department/designation-form";
    }

    @PostMapping("/{departmentId}/designations/add")
    public String addDesignation(
            @PathVariable UUID departmentId,
            @Valid @ModelAttribute("designationDto") DesignationDto dto,
            BindingResult result,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            RedirectAttributes redirectAttributes,
            Model model) {

        dto.setDepartmentId(departmentId);

        if (result.hasErrors()) {
            DepartmentDto department = departmentService.getDepartmentById(departmentId);
            Map<String, List<Authority>> authoritiesByCategory = new LinkedHashMap<>();
            for (Authority auth : Authority.values()) {
                authoritiesByCategory.computeIfAbsent(auth.getCategory(), k -> new ArrayList<>()).add(auth);
            }
            model.addAttribute("department", department);
            model.addAttribute("authoritiesByCategory", authoritiesByCategory);
            model.addAttribute("isEdit", false);
            model.addAttribute("activePage", "departments");
            return "department/designation-form";
        }

        try {
            UUID schoolId = getSchoolId(userDetails);
            DesignationDto saved = departmentService.createDesignation(dto, schoolId);
            redirectAttributes.addFlashAttribute("successMessage", "Designation '" + saved.getTitle() + "' added with " + (saved.getAuthorities() != null ? saved.getAuthorities().size() : 0) + " authorities!");
            return "redirect:/web/departments/" + departmentId;
        } catch (Exception e) {
            log.error("Error creating designation: {}", e.getMessage());
            DepartmentDto department = departmentService.getDepartmentById(departmentId);
            Map<String, List<Authority>> authoritiesByCategory = new LinkedHashMap<>();
            for (Authority auth : Authority.values()) {
                authoritiesByCategory.computeIfAbsent(auth.getCategory(), k -> new ArrayList<>()).add(auth);
            }
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("department", department);
            model.addAttribute("authoritiesByCategory", authoritiesByCategory);
            model.addAttribute("isEdit", false);
            model.addAttribute("activePage", "departments");
            return "department/designation-form";
        }
    }

    // ─────────────────────────────────────
    // DESIGNATIONS — Edit Form & Submission
    // ─────────────────────────────────────
    @GetMapping("/{departmentId}/designations/{desigId}/edit")
    public String showEditDesignationForm(
            @PathVariable UUID departmentId,
            @PathVariable UUID desigId,
            Model model) {

        DepartmentDto department = departmentService.getDepartmentById(departmentId);
        DesignationDto designationDto = departmentService.getDesignationById(desigId);

        Map<String, List<Authority>> authoritiesByCategory = new LinkedHashMap<>();
        for (Authority auth : Authority.values()) {
            authoritiesByCategory.computeIfAbsent(auth.getCategory(), k -> new ArrayList<>()).add(auth);
        }

        model.addAttribute("designationDto", designationDto);
        model.addAttribute("department", department);
        model.addAttribute("authoritiesByCategory", authoritiesByCategory);
        model.addAttribute("isEdit", true);
        model.addAttribute("activePage", "departments");

        return "department/designation-form";
    }

    @PostMapping("/{departmentId}/designations/{desigId}/edit")
    public String updateDesignation(
            @PathVariable UUID departmentId,
            @PathVariable UUID desigId,
            @Valid @ModelAttribute("designationDto") DesignationDto dto,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model) {

        dto.setDepartmentId(departmentId);

        if (result.hasErrors()) {
            DepartmentDto department = departmentService.getDepartmentById(departmentId);
            Map<String, List<Authority>> authoritiesByCategory = new LinkedHashMap<>();
            for (Authority auth : Authority.values()) {
                authoritiesByCategory.computeIfAbsent(auth.getCategory(), k -> new ArrayList<>()).add(auth);
            }
            model.addAttribute("department", department);
            model.addAttribute("authoritiesByCategory", authoritiesByCategory);
            model.addAttribute("isEdit", true);
            model.addAttribute("activePage", "departments");
            return "department/designation-form";
        }

        try {
            DesignationDto updated = departmentService.updateDesignation(desigId, dto);
            redirectAttributes.addFlashAttribute("successMessage", "Designation '" + updated.getTitle() + "' updated successfully!");
            return "redirect:/web/departments/" + departmentId;
        } catch (Exception e) {
            log.error("Error updating designation: {}", e.getMessage());
            DepartmentDto department = departmentService.getDepartmentById(departmentId);
            Map<String, List<Authority>> authoritiesByCategory = new LinkedHashMap<>();
            for (Authority auth : Authority.values()) {
                authoritiesByCategory.computeIfAbsent(auth.getCategory(), k -> new ArrayList<>()).add(auth);
            }
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("department", department);
            model.addAttribute("authoritiesByCategory", authoritiesByCategory);
            model.addAttribute("isEdit", true);
            model.addAttribute("activePage", "departments");
            return "department/designation-form";
        }
    }

    // ─────────────────────────────────────
    // DESIGNATIONS — Delete & Toggle Status
    // ─────────────────────────────────────
    @PostMapping("/{departmentId}/designations/{desigId}/delete")
    public String deleteDesignation(
            @PathVariable UUID departmentId,
            @PathVariable UUID desigId,
            RedirectAttributes redirectAttributes) {

        departmentService.deleteDesignation(desigId);
        redirectAttributes.addFlashAttribute("successMessage", "Designation deleted successfully!");
        return "redirect:/web/departments/" + departmentId;
    }

    @PostMapping("/{departmentId}/designations/{desigId}/toggle")
    public String toggleDesignationStatus(
            @PathVariable UUID departmentId,
            @PathVariable UUID desigId,
            RedirectAttributes redirectAttributes) {

        DesignationDto updated = departmentService.toggleDesignationStatus(desigId);
        String statusText = Boolean.TRUE.equals(updated.getIsActive()) ? "activated" : "deactivated";
        redirectAttributes.addFlashAttribute("successMessage", "Designation " + statusText + "!");
        return "redirect:/web/departments/" + departmentId;
    }
}
