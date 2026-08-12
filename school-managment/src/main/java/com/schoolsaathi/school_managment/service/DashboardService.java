package com.schoolsaathi.school_managment.service;

import com.schoolsaathi.school_managment.dto.response.DashboardResponseDto;

import java.util.UUID;

public interface DashboardService {

    DashboardResponseDto getSchoolAdminDashboard(UUID schoolId);
}