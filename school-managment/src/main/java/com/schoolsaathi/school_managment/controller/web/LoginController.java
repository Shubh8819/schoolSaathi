package com.schoolsaathi.school_managment.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginPage() {
        // Resolves to src/main/resources/templates/login.html
        return "login";
    }
}