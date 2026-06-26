package com.schoolsaathi.school_managment.config;


import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ThymeleafConfig {

    @Bean
    public LayoutDialect layoutDialect() {
        System.out.println(">>>>>>>> LayoutDialect Loaded <<<<<<<<");
        return new LayoutDialect();
    }
}