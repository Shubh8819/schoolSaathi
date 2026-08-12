package com.schoolsaathi.school_managment.service.serviceimpl;


import com.schoolsaathi.school_managment.dto.request.SchoolRegistrationDto;
import com.schoolsaathi.school_managment.dto.request.SchoolUpdateDto;
import com.schoolsaathi.school_managment.dto.common.DashboardDto;
import com.schoolsaathi.school_managment.dto.response.SchoolResponseDto;
import com.schoolsaathi.school_managment.entity.School;
import com.schoolsaathi.school_managment.entity.User;
import com.schoolsaathi.school_managment.enums.SubscriptionPlan;
import com.schoolsaathi.school_managment.enums.UserRole;
import com.schoolsaathi.school_managment.exception.DuplicateEntryException;
import com.schoolsaathi.school_managment.exception.SchoolNotFoundException;
import com.schoolsaathi.school_managment.repository.SchoolRepository;
import com.schoolsaathi.school_managment.repository.UserRepository;
import com.schoolsaathi.school_managment.service.SchoolService;
import com.schoolsaathi.school_managment.util.SchoolCodeGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchoolServiceImpl implements SchoolService {

    private final SchoolRepository schoolRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SchoolCodeGenerator schoolCodeGenerator;

    // ─────────────────────────────────────
    // Register School
    // ─────────────────────────────────────


    @Override
    @Transactional
    public SchoolResponseDto registerSchool( SchoolRegistrationDto dto) {
        log.info("Registering new school: {}",dto.getName());
        if (schoolRepository.existsByEmailAndIsDeletedFalse(dto.getEmail())) {
            throw new DuplicateEntryException("School already exists with email: "+ dto.getEmail());
        }
        // 2. School Code generate karo
        String schoolCode = schoolCodeGenerator.generate();

        // 3. School entity banao
        School school = School.builder()
                .schoolCode(schoolCode).name(dto.getName())
                .tagline(dto.getTagline()).email(dto.getEmail())
                .phone(dto.getPhone()).alternatePhone(dto.getAlternatePhone()).website(dto.getWebsite())
                .addressLine1(dto.getAddressLine1()).addressLine2(dto.getAddressLine2())
                .city(dto.getCity())
                .state(dto.getState())
                .pincode(dto.getPincode())
                .boardType(dto.getBoardType())
                .schoolType(dto.getSchoolType())
                .medium(dto.getMedium())
                .principalName(dto.getPrincipalName())
                .principalPhone(dto.getPrincipalPhone())
                .principalEmail(dto.getPrincipalEmail())
                // Subscription — 30 din free trial
                .subscriptionPlan(SubscriptionPlan.TRIAL)
                .isTrial(true)
                .trialStartDate(LocalDate.now())
                .trialEndDate(LocalDate.now().plusDays(30))
                .maxStudents(200)
                .isActive(true)
                // Default receipt prefix
                .receiptPrefix("REC")
                .receiptStartNumber(1)
                .primaryColor("#1a73e8")
                .build();

        // school_id set karo (BaseEntity mein hai)
        School savedSchool = schoolRepository.save(school);

        // 4. Admin user banao
        User adminUser = User.builder()
                .name(dto.getAdminName())
                .email(dto.getAdminEmail())
                .password(passwordEncoder.encode(dto.getAdminPassword()))
                .role(UserRole.SCHOOL_ADMIN)
                .isActive(true)
                .build();

        // School ID set karo user mein
        adminUser.setSchoolId(savedSchool.getId());
        userRepository.save(adminUser);

        log.info("School registered successfully: {} - {}",schoolCode, dto.getName());

        return mapToResponseDto(savedSchool);
    }

    @Override
    @Transactional
    public SchoolResponseDto updateSchool(UUID schoolId, SchoolUpdateDto dto) {

        School school = findSchoolById(schoolId);

        // Sirf jo fields aaye hain woh update karo
        if (dto.getName() != null)
            school.setName(dto.getName());
        if (dto.getTagline() != null)
            school.setTagline(dto.getTagline());
        if (dto.getPhone() != null)
            school.setPhone(dto.getPhone());
        if (dto.getAlternatePhone() != null)
            school.setAlternatePhone(
                    dto.getAlternatePhone());
        if (dto.getWebsite() != null)
            school.setWebsite(dto.getWebsite());
        if (dto.getAddressLine1() != null)
            school.setAddressLine1(dto.getAddressLine1());
        if (dto.getAddressLine2() != null)
            school.setAddressLine2(dto.getAddressLine2());
        if (dto.getCity() != null)
            school.setCity(dto.getCity());
        if (dto.getState() != null)
            school.setState(dto.getState());
        if (dto.getPincode() != null)
            school.setPincode(dto.getPincode());
        if (dto.getBoardType() != null)
            school.setBoardType(dto.getBoardType());
        if (dto.getPrincipalName() != null)
            school.setPrincipalName(
                    dto.getPrincipalName());
        if (dto.getPrincipalPhone() != null)
            school.setPrincipalPhone(
                    dto.getPrincipalPhone());
        if (dto.getBankName() != null)
            school.setBankName(dto.getBankName());
        if (dto.getAccountNumber() != null)
            school.setAccountNumber(
                    dto.getAccountNumber());
        if (dto.getIfscCode() != null)
            school.setIfscCode(dto.getIfscCode());
        if (dto.getGstNumber() != null)
            school.setGstNumber(dto.getGstNumber());
        if (dto.getLogoUrl() != null)
            school.setLogoUrl(dto.getLogoUrl());
        if (dto.getPrimaryColor() != null)
            school.setPrimaryColor(dto.getPrimaryColor());
        if (dto.getReceiptPrefix() != null)
            school.setReceiptPrefix(
                    dto.getReceiptPrefix());

        School updatedSchool =
                schoolRepository.save(school);

        log.info("School updated: {}", schoolId);

        return mapToResponseDto(updatedSchool);
    }

    // ─────────────────────────────────────
    // Get School
    // ─────────────────────────────────────

    @Override
    public SchoolResponseDto getSchoolById(
            UUID schoolId) {
        return mapToResponseDto(
                findSchoolById(schoolId));
    }

    @Override
    public SchoolResponseDto getSchoolByCode(
            String schoolCode) {
        School school = schoolRepository
                .findBySchoolCodeAndIsDeletedFalse(
                        schoolCode)
                .orElseThrow(() ->
                        new SchoolNotFoundException(
                                "School not found: "
                                        + schoolCode));
        return mapToResponseDto(school);
    }

    @Override
    public List<SchoolResponseDto> getAllSchools() {
        return schoolRepository
                .findAllByIsDeletedFalse()
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<SchoolResponseDto> getAllActiveSchools() {
        return schoolRepository
                .findAllByIsActiveTrueAndIsDeletedFalse()
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<SchoolResponseDto> searchSchools(
            String keyword) {
        return schoolRepository
                .searchSchools(keyword)
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    // ─────────────────────────────────────
    // Delete School
    // ─────────────────────────────────────

    @Override
    @Transactional
    public void deleteSchool(UUID schoolId) {
        School school = findSchoolById(schoolId);

        // Soft delete
        school.setIsDeleted(true);
        school.setIsActive(false);
        schoolRepository.save(school);

        log.info("School soft deleted: {}", schoolId);
    }

    // ─────────────────────────────────────
    // Activation
    // ─────────────────────────────────────

    @Override
    @Transactional
    public SchoolResponseDto activateSchool(
            UUID schoolId) {
        School school = findSchoolById(schoolId);
        school.setIsActive(true);
        return mapToResponseDto(
                schoolRepository.save(school));
    }

    @Override
    @Transactional
    public SchoolResponseDto deactivateSchool(
            UUID schoolId) {
        School school = findSchoolById(schoolId);
        school.setIsActive(false);
        return mapToResponseDto(
                schoolRepository.save(school));
    }

    @Override
    @Transactional
    public SchoolResponseDto extendTrial(
            UUID schoolId, Integer days) {
        School school = findSchoolById(schoolId);

        // Trial end date aage badhao
        LocalDate newEndDate = school
                .getTrialEndDate() != null
                ? school.getTrialEndDate()
                .plusDays(days)
                : LocalDate.now().plusDays(days);

        school.setTrialEndDate(newEndDate);
        school.setIsTrial(true);
        school.setIsActive(true);

        log.info("Trial extended for school: {} by {} days",
                schoolId, days);

        return mapToResponseDto(
                schoolRepository.save(school));
    }

    @Override
    @Transactional
    public SchoolResponseDto upgradePlan(
            UUID schoolId, String plan) {
        School school = findSchoolById(schoolId);

        SubscriptionPlan newPlan =
                SubscriptionPlan.valueOf(plan);

        school.setSubscriptionPlan(newPlan);
        school.setIsTrial(false);
        school.setSubscriptionStart(LocalDate.now());
        school.setSubscriptionEnd(
                LocalDate.now().plusYears(1));

        // Plan ke hisaab se max students set karo
        switch (newPlan) {
            case STARTER -> school.setMaxStudents(200);
            case GROWTH  -> school.setMaxStudents(600);
            case SCHOOL  -> school.setMaxStudents(1500);
            case CUSTOM  -> school.setMaxStudents(99999);
        }

        log.info("Plan upgraded for school: {} to {}",
                schoolId, plan);

        return mapToResponseDto(
                schoolRepository.save(school));
    }

    // ─────────────────────────────────────
    // Validation
    // ─────────────────────────────────────

    @Override
    public Boolean isEmailExists(String email) {
        return schoolRepository
                .existsByEmailAndIsDeletedFalse(email);
    }

    @Override
    public Boolean isSchoolCodeExists(
            String schoolCode) {
        return schoolRepository
                .existsBySchoolCodeAndIsDeletedFalse(
                        schoolCode);
    }

    // ─────────────────────────────────────
    // Dashboard
    // ─────────────────────────────────────

    @Override
    public DashboardDto getSuperAdminDashboard() {
        return DashboardDto.builder()
                .totalStudents(
                        schoolRepository
                                .countByIsActiveTrueAndIsDeletedFalse()
                                .intValue())
                .build();
    }

    // ─────────────────────────────────────
    // Private Helper Methods
    // ─────────────────────────────────────

    // School find karo — nahi mila toh exception
    private School findSchoolById(UUID schoolId) {
        return schoolRepository
                .findByIdAndIsDeletedFalse(schoolId)
                .orElseThrow(() ->
                        new SchoolNotFoundException(
                                "School not found: "
                                        + schoolId));
    }

    // Entity → DTO convert karo
    private SchoolResponseDto mapToResponseDto(
            School school) {
        return SchoolResponseDto.builder()
                .id(school.getId())
                .schoolCode(school.getSchoolCode())
                .name(school.getName())
                .tagline(school.getTagline())
                .email(school.getEmail())
                .phone(school.getPhone())
                .city(school.getCity())
                .state(school.getState())
                .pincode(school.getPincode())
                .boardType(school.getBoardType())
                .schoolType(school.getSchoolType())
                .medium(school.getMedium())
                .principalName(school.getPrincipalName())
                .principalPhone(school.getPrincipalPhone())
                .subscriptionPlan(
                        school.getSubscriptionPlan())
                .subscriptionEnd(
                        school.getSubscriptionEnd())
                .isActive(school.getIsActive())
                .isTrial(school.getIsTrial())
                .trialEndDate(school.getTrialEndDate())
                .logoUrl(school.getLogoUrl())
                .primaryColor(school.getPrimaryColor())
                .receiptPrefix(school.getReceiptPrefix())
                .createdAt(school.getCreatedAt())
                .build();
    }
}