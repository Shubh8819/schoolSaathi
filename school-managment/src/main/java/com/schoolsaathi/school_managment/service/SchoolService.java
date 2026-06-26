package com.schoolsaathi.school_managment.service;


import com.schoolsaathi.school_managment.dto.request.SchoolRegistrationDto;
import com.schoolsaathi.school_managment.dto.request.SchoolUpdateDto;
import com.schoolsaathi.school_managment.dto.common.ApiResponse;
import com.schoolsaathi.school_managment.dto.common.DashboardDto;
import com.schoolsaathi.school_managment.dto.response.SchoolResponseDto;

import java.util.List;
import java.util.UUID;

public interface SchoolService {

    // ─────────────────────────────────────
    // CRUD Operations
    // ─────────────────────────────────────

    // Naya school register karo
    SchoolResponseDto registerSchool(
            SchoolRegistrationDto dto
    );

    // School update karo
    SchoolResponseDto updateSchool(
            UUID schoolId,
            SchoolUpdateDto dto
    );

    // School by ID
    SchoolResponseDto getSchoolById(
            UUID schoolId
    );

    // School by Code
    SchoolResponseDto getSchoolByCode(
            String schoolCode
    );

    // Saare schools (Super Admin ke liye)
    List<SchoolResponseDto> getAllSchools();

    // Active schools
    List<SchoolResponseDto> getAllActiveSchools();

    // School search
    List<SchoolResponseDto> searchSchools(
            String keyword
    );

    // School delete (soft)
    void deleteSchool(UUID schoolId);

    // ─────────────────────────────────────
    // Activation / Subscription
    // ─────────────────────────────────────

    // School activate karo
    SchoolResponseDto activateSchool(UUID schoolId);

    // School deactivate karo
    SchoolResponseDto deactivateSchool(UUID schoolId);

    // Trial extend karo
    SchoolResponseDto extendTrial(
            UUID schoolId,
            Integer days
    );

    // Plan upgrade karo
    SchoolResponseDto upgradePlan(
            UUID schoolId,
            String plan
    );

    // ─────────────────────────────────────
    // Validation
    // ─────────────────────────────────────

    // Email already exists?
    Boolean isEmailExists(String email);

    // School code exists?
    Boolean isSchoolCodeExists(String schoolCode);

    // ─────────────────────────────────────
    // Dashboard
    // ─────────────────────────────────────

    // Super admin dashboard data
    DashboardDto getSuperAdminDashboard();
}