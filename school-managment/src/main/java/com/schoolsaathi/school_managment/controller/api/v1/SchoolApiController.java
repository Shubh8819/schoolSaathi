package com.schoolsaathi.school_managment.controller.api.v1;


import com.schoolsaathi.school_managment.dto.request.SchoolRegistrationDto;
import com.schoolsaathi.school_managment.dto.request.SchoolUpdateDto;
import com.schoolsaathi.school_managment.dto.common.ApiResponse;
import com.schoolsaathi.school_managment.dto.response.SchoolResponseDto;
import com.schoolsaathi.school_managment.service.SchoolService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.util.List;
import java.util.UUID;


import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@Slf4j
@RestController
@RequestMapping("/api/v1/schools")
@RequiredArgsConstructor
public class SchoolApiController {


    private final SchoolService schoolService;
    @Autowired
    SchoolApiController(SchoolService schoolService, SchoolService schoolService1){

        this.schoolService = schoolService1;
    }

    // GET ALL SCHOOLS
    @GetMapping
    public ResponseEntity<ApiResponse<List<SchoolResponseDto>>> getAllSchools(
            @RequestParam(required = false) String keyword) {

        //log.info("Fetching all schools");

        List<SchoolResponseDto> schools =
                (keyword != null && !keyword.isEmpty())
                        ? schoolService.searchSchools(keyword)
                        : schoolService.getAllSchools();

        return ResponseEntity.ok(
                ApiResponse.success("Schools fetched", schools)
        );
    }

    // GET ACTIVE SCHOOLS
    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<SchoolResponseDto>>> getActiveSchools() {

        List<SchoolResponseDto> schools =
                schoolService.getAllActiveSchools();

        return ResponseEntity.ok(
                ApiResponse.success("Active schools fetched", schools)
        );
    }

    // GET BY ID
    @GetMapping("/{schoolId}")
    public ResponseEntity<ApiResponse<SchoolResponseDto>> getSchoolById(
            @PathVariable UUID schoolId) {

        //log.info("Fetching school: {}", schoolId);

        SchoolResponseDto school =
                schoolService.getSchoolById(schoolId);

        return ResponseEntity.ok(
                ApiResponse.success("School fetched", school)
        );
    }

    // GET BY CODE
    @GetMapping("/code/{schoolCode}")
    public ResponseEntity<ApiResponse<SchoolResponseDto>> getSchoolByCode(
            @PathVariable String schoolCode) {

        SchoolResponseDto school =
                schoolService.getSchoolByCode(schoolCode);

        return ResponseEntity.ok(
                ApiResponse.success("School fetched", school)
        );
    }

    // REGISTER NEW SCHOOL
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<SchoolResponseDto>> registerSchool(
            @Valid @RequestBody SchoolRegistrationDto dto) {

        //log.info("Registering school: {}", dto.getName());

        SchoolResponseDto school =
                schoolService.registerSchool(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        "School registered successfully", school)
                );
    }

    // UPDATE SCHOOL
    @PutMapping("/{schoolId}")
    public ResponseEntity<ApiResponse<SchoolResponseDto>> updateSchool(
            @PathVariable UUID schoolId,
            @Valid @RequestBody SchoolUpdateDto dto) {

        //log.info("Updating school: {}", schoolId);

        SchoolResponseDto school =
                schoolService.updateSchool(schoolId, dto);

        return ResponseEntity.ok(
                ApiResponse.success("School updated", school)
        );
    }

    // ACTIVATE
    @PatchMapping("/{schoolId}/activate")
    public ResponseEntity<ApiResponse<SchoolResponseDto>> activateSchool(
            @PathVariable UUID schoolId) {

        SchoolResponseDto school =
                schoolService.activateSchool(schoolId);

        return ResponseEntity.ok(
                ApiResponse.success("School activated", school)
        );
    }

    // DEACTIVATE
    @PatchMapping("/{schoolId}/deactivate")
    public ResponseEntity<ApiResponse<SchoolResponseDto>> deactivateSchool(
            @PathVariable UUID schoolId) {

        SchoolResponseDto school =
                schoolService.deactivateSchool(schoolId);

        return ResponseEntity.ok(
                ApiResponse.success("School deactivated", school)
        );
    }

    // EXTEND TRIAL
    @PatchMapping("/{schoolId}/extend-trial")
    public ResponseEntity<ApiResponse<SchoolResponseDto>> extendTrial(
            @PathVariable UUID schoolId,
            @RequestParam Integer days) {

        SchoolResponseDto school =
                schoolService.extendTrial(schoolId, days);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Trial extended by " + days + " days", school)
        );
    }

    // UPGRADE PLAN
    @PatchMapping("/{schoolId}/upgrade")
    public ResponseEntity<ApiResponse<SchoolResponseDto>> upgradePlan(
            @PathVariable UUID schoolId,
            @RequestParam String plan) {

        SchoolResponseDto school =
                schoolService.upgradePlan(schoolId, plan);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Plan upgraded to " + plan, school)
        );
    }

    // DELETE
    @DeleteMapping("/{schoolId}")
    public ResponseEntity<ApiResponse<Void>> deleteSchool(
            @PathVariable UUID schoolId) {

        schoolService.deleteSchool(schoolId);

        return ResponseEntity.ok(
                ApiResponse.success("School deleted", null)
        );
    }

    // CHECK EMAIL
    @GetMapping("/check-email")
    public ResponseEntity<ApiResponse<Boolean>> checkEmail(
            @RequestParam String email) {

        Boolean exists = schoolService.isEmailExists(email);

        return ResponseEntity.ok(
                ApiResponse.success(
                        exists ? "Email already registered"
                                : "Email available",
                        exists)
        );
    }

    // CHECK CODE
    @GetMapping("/check-code")
    public ResponseEntity<ApiResponse<Boolean>> checkSchoolCode(
            @RequestParam String code) {

        Boolean exists = schoolService.isSchoolCodeExists(code);

        return ResponseEntity.ok(
                ApiResponse.success(
                        exists ? "Code already taken"
                                : "Code available",
                        exists)
        );
    }
}