package com.schoolsaathi.school_managment.controller.web;

import com.schoolsaathi.school_managment.dto.response.DashboardResponseDto;
import com.schoolsaathi.school_managment.security.CustomUserDetails;
import com.schoolsaathi.school_managment.service.DashboardService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/web/teacher/")
public class TeacherController {

    @Autowired
    private DashboardService dashboardService;

    @RequestMapping("dashboard")
    public String teacherDashboard(@AuthenticationPrincipal CustomUserDetails userDetails, Model model, HttpServletRequest request){
        System.out.println("Teacher Deshboorad   ");
        model.addAttribute("userDetails", userDetails);
        DashboardResponseDto dashboard = dashboardService.getSchoolAdminDashboard(userDetails.getSchoolId());

        model.addAttribute("dashboard", dashboard);
        model.addAttribute("activePage", "dashboard");
        return "dashboard/teacher-dashboard";

    }
}
