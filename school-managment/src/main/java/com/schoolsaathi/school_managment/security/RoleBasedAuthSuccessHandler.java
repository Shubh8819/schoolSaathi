package com.schoolsaathi.school_managment.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
@Slf4j

public class RoleBasedAuthSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,HttpServletResponse response,Authentication authentication)
            throws IOException, ServletException {
        String redirectUrl = "/web/dashboard"; // generic fallback
        System.out.println("authentication.getAuthorities()"+authentication.getAuthorities());
        Optional<String> roleAuthority = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(a -> a.startsWith("ROLE_"))
                .findFirst();

        if (roleAuthority.isPresent()) {
            String role = roleAuthority.get();
            log.info("User logged in with role: {}", role);

            redirectUrl = switch (role) {
                case "ROLE_SUPER_ADMIN" -> "/web/super-admin/dashboard";
                case "ROLE_ADMIN" -> "/web/admin/dashboard";
                case "ROLE_ACCOUNTANT" -> "/web/account/dashboard";
                case "ROLE_TEACHER" -> "/web/teacher/dashboard";
                case "ROLE_STUDENT" -> "/web/student/dashboard";
                default -> "/web/dashboard";
            };
        }
        String fullRedirectUrl = request.getContextPath() + redirectUrl;
        log.info("Redirecting to: {}", fullRedirectUrl);
        response.sendRedirect(fullRedirectUrl);
    }
}