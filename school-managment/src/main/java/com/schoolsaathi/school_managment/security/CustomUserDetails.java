package com.schoolsaathi.school_managment.security;

import com.schoolsaathi.school_managment.entity.User;
import com.schoolsaathi.school_managment.enums.Authority;
import com.schoolsaathi.school_managment.enums.UserRole;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Getter
public class CustomUserDetails implements UserDetails {

    private final UUID userId;
    private final UUID schoolId;
    private final String name;
    private final String email;
    private final String password;
    private final UserRole role;
    private final Boolean isActive;
    private final UUID assignedClassId;
    private final UUID assignedSectionId;
    private final Set<String> authorityNames;

    public CustomUserDetails(User user) {
        this.userId = user.getId();
        this.schoolId = user.getSchoolId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.role = user.getRole();
        this.isActive = user.getIsActive();

        this.assignedClassId = user.getAssignedClass() != null ? user.getAssignedClass().getId() : null;
        this.assignedSectionId = user.getAssignedSection() != null ? user.getAssignedSection().getId() : null;

        Set<String> auths = new HashSet<>();
        // Add Role authority
        auths.add("ROLE_" + role.name());

        // Add Designation authorities
        if (user.getDesignationEntity() != null && user.getDesignationEntity().getAuthorities() != null) {
            for (Authority auth : user.getDesignationEntity().getAuthorities()) {
                auths.add(auth.name());
            }
        }

        // Super Admin and School Admin get full authorities
        if (role == UserRole.SUPER_ADMIN || role == UserRole.SCHOOL_ADMIN) {
            for (Authority auth : Authority.values()) {
                auths.add(auth.name());
            }
        }

        this.authorityNames = Collections.unmodifiableSet(auths);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (String auth : authorityNames) {
            grantedAuthorities.add(new SimpleGrantedAuthority(auth));
        }
        return grantedAuthorities;
    }

    public boolean hasAuthority(String authority) {
        if (authority == null) return false;
        return authorityNames.contains(authority);
    }

    public boolean hasAnyAuthority(String... authorities) {
        if (authorities == null) return false;
        for (String auth : authorities) {
            if (authorityNames.contains(auth)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isActive != null && isActive;
    }

    public boolean isSuperAdmin() {
        return role == UserRole.SUPER_ADMIN;
    }

    public boolean isSchoolAdmin() {
        return role == UserRole.SCHOOL_ADMIN;
    }

    public boolean isAccountant() { return role == UserRole.ACCOUNTANT;}

    public boolean isTeacher() {
        return role == UserRole.TEACHER;
    }
}