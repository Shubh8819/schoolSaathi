package com.schoolsaathi.school_managment.controller.web;


import com.schoolsaathi.school_managment.dto.request.SchoolRegistrationDto;
import com.schoolsaathi.school_managment.dto.request.SchoolUpdateDto;
import com.schoolsaathi.school_managment.dto.response.SchoolResponseDto;
import com.schoolsaathi.school_managment.enums.*;
import com.schoolsaathi.school_managment.service.SchoolService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;



import java.util.List;
import java.util.UUID;

@Slf4j
@Controller
@RequestMapping("/web/schools")
@RequiredArgsConstructor
public class SchoolWebController {

    private final SchoolService schoolService;

    // ─────────────────────────────────────
    // LIST — Saare Schools
    // ─────────────────────────────────────

    @GetMapping
    public String listSchools(
            @RequestParam(required = false)
            String keyword,
            Model model) {

        List<SchoolResponseDto> schools;

        if (keyword != null && !keyword.isEmpty()) {
            schools = schoolService
                    .searchSchools(keyword);
            model.addAttribute("keyword", keyword);
        } else {
            schools = schoolService.getAllSchools();
        }

        model.addAttribute("schools", schools);
        model.addAttribute("totalSchools",
                schools.size());

        return "school/list";
    }

    // ─────────────────────────────────────
    // DETAIL — School Detail Page
    // ─────────────────────────────────────

    @GetMapping("/{schoolId}")
    public String schoolDetail(
            @PathVariable UUID schoolId,
            Model model) {

        SchoolResponseDto school =
                schoolService.getSchoolById(schoolId);

        model.addAttribute("school", school);

        return "school/detail";
    }

    // ─────────────────────────────────────
    // ADD — Naya School Form
    // ─────────────────────────────────────

    @GetMapping("/add")
    public String showAddForm(Model model) {

        model.addAttribute("schoolDto",new SchoolRegistrationDto());
        model.addAttribute("boardTypes",com.schoolsaathi.school_managment.enums.BoardType.values());
        model.addAttribute("schoolTypes",com.schoolsaathi.school_managment.enums.SchoolType.values());

        return "school/add";
    }

    @PostMapping("/add")
    public String addSchool(@Valid @ModelAttribute("schoolDto") SchoolRegistrationDto dto, BindingResult result,RedirectAttributes redirectAttributes,Model model) {

        // Validation errors
        if (result.hasErrors()) {
            model.addAttribute("boardTypes",BoardType.values());
            model.addAttribute("schoolTypes",SchoolType.values());
            return "school/add";
        }
        // Email already exists check
        if (schoolService.isEmailExists(dto.getEmail())) {
            result.rejectValue("email","duplicate","Email already registered");
            return "school/add";
        }

        try {
            SchoolResponseDto school = schoolService.registerSchool(dto);
            redirectAttributes.addFlashAttribute("successMessage","School registered successfully! "+ "Code: "+ school.getSchoolCode());
            return "redirect:/web/schools";
        } catch (Exception e) {
            log.error("Error registering school: {}",e.getMessage());
            model.addAttribute("errorMessage","Something went wrong. Try again.");
            return "school/add";
        }
    }

    // ─────────────────────────────────────
    // EDIT — School Update Form
    // ─────────────────────────────────────

    @GetMapping("/{schoolId}/edit")
    public String showEditForm(
            @PathVariable UUID schoolId,
            Model model) {

        SchoolResponseDto school =
                schoolService.getSchoolById(schoolId);

        // Response DTO → Update DTO convert
        SchoolUpdateDto updateDto = SchoolUpdateDto
                .builder()
                .name(school.getName())
                .phone(school.getPhone())
                .city(school.getCity())
                .state(school.getState())
                .pincode(school.getPincode())
                .principalName(school.getPrincipalName())
                .principalPhone(
                        school.getPrincipalPhone())
                .logoUrl(school.getLogoUrl())
                .primaryColor(school.getPrimaryColor())
                .receiptPrefix(school.getReceiptPrefix())
                .build();

        model.addAttribute("school", school);
        model.addAttribute("updateDto", updateDto);
        model.addAttribute("boardTypes",
                com.schoolsaathi.school_managment.enums
                        .BoardType.values());
        model.addAttribute("schoolTypes",
                com.schoolsaathi.school_managment.enums
                        .SchoolType.values());

        return "school/edit";
    }

    @PostMapping("/{schoolId}/edit")
    public String updateSchool(
            @PathVariable UUID schoolId,
            @Valid @ModelAttribute("updateDto")
            SchoolUpdateDto dto,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (result.hasErrors()) {
            return "school/edit";
        }

        try {
            schoolService.updateSchool(schoolId, dto);

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "School updated successfully!"
            );

            return "redirect:/web/schools/" + schoolId;

        } catch (Exception e) {
            log.error("Error updating school: {}",
                    e.getMessage());
            model.addAttribute(
                    "errorMessage",
                    "Update failed. Try again."
            );
            return "school/edit";
        }
    }

    // ─────────────────────────────────────
    // ACTIVATE / DEACTIVATE
    // ─────────────────────────────────────

    @PostMapping("/{schoolId}/activate")
    public String activateSchool(
            @PathVariable UUID schoolId,
            RedirectAttributes redirectAttributes) {

        schoolService.activateSchool(schoolId);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "School activated successfully!"
        );

        return "redirect:/web/schools/" + schoolId;
    }

    @PostMapping("/{schoolId}/deactivate")
    public String deactivateSchool(
            @PathVariable UUID schoolId,
            RedirectAttributes redirectAttributes) {

        schoolService.deactivateSchool(schoolId);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "School deactivated!"
        );

        return "redirect:/web/schools/" + schoolId;
    }

    // ─────────────────────────────────────
    // TRIAL EXTEND
    // ─────────────────────────────────────

    @PostMapping("/{schoolId}/extend-trial")
    public String extendTrial(
            @PathVariable UUID schoolId,
            @RequestParam Integer days,
            RedirectAttributes redirectAttributes) {

        schoolService.extendTrial(schoolId, days);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Trial extended by " + days + " days!"
        );

        return "redirect:/web/schools/" + schoolId;
    }

    // ─────────────────────────────────────
    // UPGRADE PLAN
    // ─────────────────────────────────────

    @PostMapping("/{schoolId}/upgrade")
    public String upgradePlan(
            @PathVariable UUID schoolId,
            @RequestParam String plan,
            RedirectAttributes redirectAttributes) {

        schoolService.upgradePlan(schoolId, plan);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Plan upgraded to " + plan + "!"
        );

        return "redirect:/web/schools/" + schoolId;
    }

    // ─────────────────────────────────────
    // DELETE
    // ─────────────────────────────────────

    @PostMapping("/{schoolId}/delete")
    public String deleteSchool(
            @PathVariable UUID schoolId,
            RedirectAttributes redirectAttributes) {

        schoolService.deleteSchool(schoolId);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "School deleted successfully!"
        );

        return "redirect:/web/schools";
    }
}