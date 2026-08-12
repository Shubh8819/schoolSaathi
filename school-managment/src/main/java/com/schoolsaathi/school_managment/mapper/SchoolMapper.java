package com.schoolsaathi.school_managment.mapper;

import com.schoolsaathi.school_managment.dto.response.SchoolResponseDto;
import com.schoolsaathi.school_managment.entity.School;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
public class SchoolMapper {


    public SchoolResponseDto toDto(School school) {

        if (school == null) {
            return null;
        }

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
                .subscriptionPlan(school.getSubscriptionPlan())
                .subscriptionEnd(school.getSubscriptionEnd())
                .isActive(school.getIsActive())
                .isTrial(school.getIsTrial())
                .trialEndDate(school.getTrialEndDate())
                .logoUrl(school.getLogoUrl())
                .primaryColor(school.getPrimaryColor())
                .receiptPrefix(school.getReceiptPrefix())
                .createdAt(school.getCreatedAt())
                .build();
    }

    public School toEntity(SchoolResponseDto dto) {

        if (dto == null) {
            return null;
        }

        return School.builder()
                .schoolCode(dto.getSchoolCode())
                .name(dto.getName())
                .tagline(dto.getTagline())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .city(dto.getCity())
                .state(dto.getState())
                .pincode(dto.getPincode())
                .boardType(dto.getBoardType())
                .schoolType(dto.getSchoolType())
                .medium(dto.getMedium())
                .principalName(dto.getPrincipalName())
                .principalPhone(dto.getPrincipalPhone())
                .subscriptionPlan(dto.getSubscriptionPlan())
                .subscriptionEnd(dto.getSubscriptionEnd())
                .isActive(dto.getIsActive())
                .isTrial(dto.getIsTrial())
                .trialEndDate(dto.getTrialEndDate())
                .logoUrl(dto.getLogoUrl())
                .primaryColor(dto.getPrimaryColor())
                .receiptPrefix(dto.getReceiptPrefix())
                .build();
    }

}
