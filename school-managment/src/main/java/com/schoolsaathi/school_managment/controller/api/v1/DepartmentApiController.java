package com.schoolsaathi.school_managment.controller.api.v1;

import com.schoolsaathi.school_managment.dto.common.ApiResponse;
import com.schoolsaathi.school_managment.dto.request.DepartmentDto;
import com.schoolsaathi.school_managment.dto.request.DesignationDto;
import com.schoolsaathi.school_managment.enums.Authority;
import com.schoolsaathi.school_managment.security.CustomUserDetails;
import com.schoolsaathi.school_managment.service.DepartmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
public class DepartmentApiController {

    private final DepartmentService departmentService;

    // GET ALL DEPARTMENTS
    @GetMapping
    public ResponseEntity<ApiResponse<List<DepartmentDto>>> getAllDepartments(
            @RequestParam(required = false) String keyword,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UUID schoolId = userDetails != null ? userDetails.getSchoolId() : null;
        List<DepartmentDto> departments = (keyword != null && !keyword.isEmpty())
                ? departmentService.searchDepartments(schoolId, keyword)
                : departmentService.getAllDepartments(schoolId);

        return ResponseEntity.ok(ApiResponse.success("Departments retrieved", departments));
    }

    // GET DEPARTMENT BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentDto>> getDepartmentById(@PathVariable UUID id) {
        DepartmentDto department = departmentService.getDepartmentById(id);
        return ResponseEntity.ok(ApiResponse.success("Department retrieved", department));
    }

    // CREATE DEPARTMENT
    @PostMapping
    public ResponseEntity<ApiResponse<DepartmentDto>> createDepartment(
            @Valid @RequestBody DepartmentDto dto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        UUID schoolId = userDetails != null ? userDetails.getSchoolId() : null;
        DepartmentDto created = departmentService.createDepartment(dto, schoolId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Department created successfully", created));
    }

    // UPDATE DEPARTMENT
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DepartmentDto>> updateDepartment(
            @PathVariable UUID id,
            @Valid @RequestBody DepartmentDto dto) {

        DepartmentDto updated = departmentService.updateDepartment(id, dto);
        return ResponseEntity.ok(ApiResponse.success("Department updated successfully", updated));
    }

    // DELETE DEPARTMENT
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteDepartment(@PathVariable UUID id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.ok(ApiResponse.success("Department deleted successfully", null));
    }

    // TOGGLE DEPARTMENT STATUS
    @PatchMapping("/{id}/toggle-status")
    public ResponseEntity<ApiResponse<DepartmentDto>> toggleDepartmentStatus(@PathVariable UUID id) {
        DepartmentDto updated = departmentService.toggleDepartmentStatus(id);
        return ResponseEntity.ok(ApiResponse.success("Department status updated", updated));
    }

    // ─────────────────────────────────────
    // DESIGNATIONS & AUTHORITIES
    // ─────────────────────────────────────

    // GET DESIGNATIONS BY DEPARTMENT
    @GetMapping("/{departmentId}/designations")
    public ResponseEntity<ApiResponse<List<DesignationDto>>> getDesignationsByDepartment(@PathVariable UUID departmentId) {
        List<DesignationDto> designations = departmentService.getDesignationsByDepartment(departmentId);
        return ResponseEntity.ok(ApiResponse.success("Designations retrieved", designations));
    }

    // CREATE DESIGNATION
    @PostMapping("/{departmentId}/designations")
    public ResponseEntity<ApiResponse<DesignationDto>> createDesignation(
            @PathVariable UUID departmentId,
            @Valid @RequestBody DesignationDto dto,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        dto.setDepartmentId(departmentId);
        UUID schoolId = userDetails != null ? userDetails.getSchoolId() : null;
        DesignationDto created = departmentService.createDesignation(dto, schoolId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Designation created successfully", created));
    }

    // UPDATE DESIGNATION
    @PutMapping("/designations/{desigId}")
    public ResponseEntity<ApiResponse<DesignationDto>> updateDesignation(
            @PathVariable UUID desigId,
            @Valid @RequestBody DesignationDto dto) {

        DesignationDto updated = departmentService.updateDesignation(desigId, dto);
        return ResponseEntity.ok(ApiResponse.success("Designation updated successfully", updated));
    }

    // DELETE DESIGNATION
    @DeleteMapping("/designations/{desigId}")
    public ResponseEntity<ApiResponse<Void>> deleteDesignation(@PathVariable UUID desigId) {
        departmentService.deleteDesignation(desigId);
        return ResponseEntity.ok(ApiResponse.success("Designation deleted successfully", null));
    }

    // LIST ALL AVAILABLE AUTHORITIES
    @GetMapping("/authorities")
    public ResponseEntity<ApiResponse<List<Authority>>> getAllAuthorities() {
        return ResponseEntity.ok(ApiResponse.success("Authorities list", Arrays.asList(Authority.values())));
    }
}
