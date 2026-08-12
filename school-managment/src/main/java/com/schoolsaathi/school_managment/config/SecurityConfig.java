package com.schoolsaathi.school_managment.config;

import com.schoolsaathi.school_managment.entity.User;
import com.schoolsaathi.school_managment.repository.UserRepository;
import com.schoolsaathi.school_managment.security.CustomUserDetailsService;
import com.schoolsaathi.school_managment.security.RoleBasedAuthSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.http.HttpStatus;

/**
 * Central Security configuration for SchoolSaathi.
 * - Form-login based authentication (session based, not JWT) since this is a
 *   server-rendered Thymeleaf app.
 * - Role-based URL authorization (centralized here, NOT scattered @PreAuthorize).
 * - Multi-tenant safe: schoolId is resolved from the logged-in CustomUserDetails,
 *   never from a request parameter.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    @Autowired
    private  UserRepository userRepository;



    @Autowired
    private RoleBasedAuthSuccessHandler roleBasedAuthSuccessHandler;

    // ---------- Password Encoder ----------
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ---------- Authentication Provider ----------
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // ---------- Authentication Manager (needed if you ever do programmatic login) ----------
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // ---------- Main Security Filter Chain ----------
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authenticationProvider(authenticationProvider())

                .authorizeHttpRequests(auth -> auth
                        // Public assets & login page
                        .requestMatchers(
                                "/", "/css/**", "/js/**", "/images/**", "/webjars/**",
                                "/login", "/access-denied", "/error"
                        ).permitAll()
                        // Role-restricted areas (adjust prefixes to match your controllers)
                        .requestMatchers("/web/admin/**","/web/students/**").hasAnyRole("SCHOOL_ADMIN", "SUPER_ADMIN","ACCOUNTANT","TEACHER")
                        .requestMatchers("/web/teacher/**").hasAnyRole("TEACHER", "SCHOOL_ADMIN", "SUPER_ADMIN")
                        .requestMatchers("/web/fee/**").hasAnyRole("SCHOOL_ADMIN", "ACCOUNTANT", "SUPER_ADMIN")
                        .requestMatchers("/web/**").authenticated()

                        // Anything else needs login
                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/login")                 // custom login page (GET)
                        .loginProcessingUrl("/login")         // Spring intercepts POST here
                        .successHandler(roleBasedAuthSuccessHandler)   // role ke hisaab se redirect
                        .failureUrl("/login?error=true")
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .maximumSessions(1)                   // ek time pe ek hi device se login
                        .expiredUrl("/login?expired=true")
                )

                // CSRF stays ENABLED (default) since this is a form-based Thymeleaf app.
                // Do NOT disable it globally — only exclude specific stateless API paths if needed later:
                // .csrf(csrf -> csrf.ignoringRequestMatchers("/api/webhook/**"))

                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                        .accessDeniedPage("/access-denied")   // wrong-role access ke liye
                );

        return http.build();
    }
}