package com.schoolsaathi.school_managment.controller.web;

import com.schoolsaathi.school_managment.dto.response.DashboardResponseDto;
import com.schoolsaathi.school_managment.enums.UserRole;
import com.schoolsaathi.school_managment.security.CustomUserDetails;
import com.schoolsaathi.school_managment.service.DashboardService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class DashboardWebController {

    private final DashboardService dashboardService;

    // ─── School Admin / Principal / Vice Principal / Teacher / Accountant ───
    @GetMapping("/web/admin/dashboard")
    public String adminDashboard(@AuthenticationPrincipal CustomUserDetails userDetails,Model model,  HttpServletRequest request) {

        log.info("Loading dashboard for role: {} schoolId: {}",userDetails.getRole(), userDetails.getSchoolId());
        HttpSession session = request.getSession();
        session.setAttribute("userDetails",userDetails);

        DashboardResponseDto dashboard = dashboardService.getSchoolAdminDashboard(userDetails.getSchoolId());

        model.addAttribute("dashboard", dashboard);
        model.addAttribute("userDetails", userDetails);
        model.addAttribute("activePage", "dashboard");

        UserRole role = userDetails.getRole();
        System.out.println("role========================"+role);


        // Return role-specific dashboard view
        return switch (role) {
            case PRINCIPAL, VICE_PRINCIPAL, SCHOOL_ADMIN -> "dashboard/admin-dashboard";
            case TEACHER -> "dashboard/teacher-dashboard";
            case ACCOUNTANT -> "dashboard/accountant-dashboard";
            case LIBRARIAN -> "dashboard/librarian-dashboard";
            case RECEPTIONIST -> "dashboard/receptionist-dashboard";
            case STUDENT -> "dashboard/student-dashboard";
            case PARENT -> "dashboard/parent-dashboard";
            default -> "dashboard/admin-dashboard";
        };
    }

    // ─── Super Admin ───────────────────────────────────────────────────────
    @GetMapping("/web/super-admin/dashboard")
    public String superAdminDashboard(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model) {

        log.info("Loading Super Admin dashboard for user: {}", userDetails.getEmail());

        DashboardResponseDto dashboard = dashboardService
                .getSchoolAdminDashboard(userDetails.getSchoolId());

        model.addAttribute("dashboard", dashboard);
        model.addAttribute("userDetails", userDetails);
        model.addAttribute("activePage", "dashboard");

        return "dashboard/super-admin-dashboard";
    }

    // ─── Generic fallback ─────────────────────────────────────────────────
    @GetMapping("/web/dashboard")
    public String genericDashboard(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model) {

        if (userDetails == null) {
            return "redirect:/login";
        }

        return switch (userDetails.getRole()) {
            case SUPER_ADMIN -> "redirect:/web/super-admin/dashboard";
            default -> "redirect:/web/admin/dashboard";
        };
    }
}