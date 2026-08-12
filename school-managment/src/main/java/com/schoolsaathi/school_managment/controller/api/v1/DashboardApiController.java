package com.schoolsaathi.school_managment.controller.api.v1;

import com.schoolsaathi.school_managment.dto.common.ApiResponse;
import com.schoolsaathi.school_managment.dto.response.DashboardResponseDto;
import com.schoolsaathi.school_managment.security.CustomUserDetails;
import com.schoolsaathi.school_managment.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardApiController {

    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<ApiResponse<DashboardResponseDto>>
    getDashboard(
            @AuthenticationPrincipal
            CustomUserDetails userDetails) {

        DashboardResponseDto dashboard =
                dashboardService.getSchoolAdminDashboard(
                        userDetails.getSchoolId());

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Dashboard data fetched",
                        dashboard)
        );
    }
}