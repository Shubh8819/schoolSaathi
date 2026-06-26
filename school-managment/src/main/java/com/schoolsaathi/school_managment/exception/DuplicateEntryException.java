package com.schoolsaathi.school_managment.exception;

public class DuplicateEntryException
        extends RuntimeException {
    public DuplicateEntryException(String message) {
        super(message);
    }
}