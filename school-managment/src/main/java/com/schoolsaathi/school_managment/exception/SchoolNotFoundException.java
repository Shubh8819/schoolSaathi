package com.schoolsaathi.school_managment.exception;

public class SchoolNotFoundException
        extends RuntimeException {
    public SchoolNotFoundException(String message) {
        super(message);
    }
}