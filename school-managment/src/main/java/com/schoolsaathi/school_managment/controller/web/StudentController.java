package com.schoolsaathi.school_managment.controller.web;

import com.schoolsaathi.school_managment.dto.request.StudentRegistrationDto;
import com.schoolsaathi.school_managment.dto.response.StudentResponseDto;
import com.schoolsaathi.school_managment.entity.AcademicYear;
import com.schoolsaathi.school_managment.entity.ClassRoom;
import com.schoolsaathi.school_managment.entity.Section;
import com.schoolsaathi.school_managment.enums.AdmissionCategory;
import com.schoolsaathi.school_managment.enums.Gender;
import com.schoolsaathi.school_managment.exception.DuplicateResourceException;
// TODO: adjust to your actual repositories/package
import com.schoolsaathi.school_managment.repository.AcademicYearRepository;
import com.schoolsaathi.school_managment.repository.ClassRoomRepository;
import com.schoolsaathi.school_managment.repository.SectionRepository;
import com.schoolsaathi.school_managment.security.CustomUserDetails;
import com.schoolsaathi.school_managment.service.StudentService;
// import com.schoolsaathi.school_managment.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * NOTE: Replace `Object principal` and the two resolve*() TODOs with your actual
 * CustomUserDetails calls. Left generic since CustomUserDetailsService internals
 * aren't in this context.
 */
@Controller

@RequestMapping("/web/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;
    private final ClassRoomRepository classRoomRepository;
    private final SectionRepository sectionRepository;
    private final AcademicYearRepository academicYearRepository;

    @GetMapping
    public String listStudents(@RequestParam(required = false) String keyword, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,
                               @AuthenticationPrincipal CustomUserDetails principal, Model model) {
        UUID schoolId = principal.getSchoolId();
        Pageable pageable = PageRequest.of(page, size);

        Page<StudentResponseDto> students = studentService.searchStudents(schoolId, keyword, pageable);

        model.addAttribute("students", students);
        model.addAttribute("keyword", keyword);
        model.addAttribute("totalStudents", studentService.getTotalStudentCount(schoolId));
        model.addAttribute("activePage", "student-list");
        return "student/student-list";
    }

    @GetMapping("/add")
    public String showAddForm(@AuthenticationPrincipal CustomUserDetails principal, Model model) {
        model.addAttribute("studentRegistrationDto", new StudentRegistrationDto());
        addFormReferenceData(principal, model);
        return "student/add-student";
    }

    @PostMapping("/add")
    public String addStudent(@Valid @ModelAttribute StudentRegistrationDto studentRegistrationDto,
                              BindingResult bindingResult,
                              @RequestParam(required = false) MultipartFile photo,
                              @RequestParam(required = false) MultipartFile birthCertificate,
                              @RequestParam(required = false) MultipartFile transferCertificate,
                              @RequestParam(required = false) MultipartFile aadhaarCard,
                              @AuthenticationPrincipal CustomUserDetails principal,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            addFormReferenceData(principal, model);
            return "student/add-student";
        }

        UUID schoolId = principal.getSchoolId();
        String createdBy = resolveUsername(principal);

        try {
            StudentResponseDto saved = studentService.registerStudent(
                    studentRegistrationDto, schoolId, createdBy, photo, birthCertificate, transferCertificate, aadhaarCard);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Student '" + saved.getName() + "' registered successfully (Admission No: " + saved.getAdmissionNumber() + ").");
            return "redirect:/students";
        } catch (DuplicateResourceException ex) {
            bindingResult.rejectValue("admissionNumber", "duplicate", ex.getMessage());
            addFormReferenceData(principal, model);

            return "student/add-student";
        }
    }

    @GetMapping("/{id}")
    public String viewStudent(@PathVariable UUID id,@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        UUID schoolId = userDetails.getSchoolId();
        model.addAttribute("student", studentService.getStudentById(id, schoolId));
        return "student/student-detail";
    }

    @PostMapping("/{id}/delete")
    public String deleteStudent(@PathVariable UUID id,
                                @AuthenticationPrincipal CustomUserDetails userDetails,
                                 RedirectAttributes redirectAttributes) {
        UUID schoolId = userDetails.getSchoolId();
        studentService.deleteStudent(id, schoolId, String.valueOf(userDetails.getUserId()));
        redirectAttributes.addFlashAttribute("successMessage", "Student removed successfully.");
        return "redirect:/students";
    }

    private void addFormReferenceData(CustomUserDetails principal, Model model) {
        UUID schoolId = principal.getSchoolId();
        model.addAttribute("genders", Gender.values());
        model.addAttribute("categories", AdmissionCategory.values());
        // TODO: confirm these repository method names match your ClassRoom/Section/AcademicYear repos
        List<ClassRoom> classes=  classRoomRepository.findBySchoolId(schoolId);
        List<AcademicYear> academicYears=academicYearRepository.findAllBySchoolIdAndIsDeletedFalse(schoolId).get();
        System.out.println("classes============"+classes);

        model.addAttribute("classRooms", classes);

       // model.addAttribute("academicYears", academicYears);
    }

    // ---- TODO: wire these to your actual CustomUserDetails principal ----


    private String resolveUsername(Object principal) {
        // return ((CustomUserDetails) principal).getUsername();
        throw new UnsupportedOperationException("Wire resolveUsername() to your CustomUserDetails principal");
    }
}
