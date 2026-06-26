package com.schoolsaathi.school_managment.util;


import com.schoolsaathi.school_managment.repository.SchoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SchoolCodeGenerator {

    private final SchoolRepository schoolRepository;

    public String generate() {
        // SCH001, SCH002 format
        long count = schoolRepository.count() + 1;
        return String.format("SCH%03d", count);
    }
}