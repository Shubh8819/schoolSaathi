package com.schoolsaathi.school_managment.enums;

import lombok.Getter;

@Getter
public enum Authority {

    // Teacher Authorities
    ADD_TEACHER("Add Teacher", "Teacher Management"),
    VIEW_TEACHER("View Teacher", "Teacher Management"),
    EDIT_TEACHER("Edit Teacher", "Teacher Management"),
    DELETE_TEACHER("Delete Teacher", "Teacher Management"),

    // Student Authorities
    ADD_STUDENT("Add Student", "Student Management"),
    VIEW_STUDENT("View Student", "Student Management"),
    EDIT_STUDENT("Edit Student", "Student Management"),
    DELETE_STUDENT("Delete Student", "Student Management"),

    // Fee Authorities
    ADD_FEE("Add Fee", "Fee Management"),
    UPDATE_FEE("Update Fee", "Fee Management"),
    VIEW_FEE("View Fee", "Fee Management"),

    // Attendance Authorities
    MARK_ATTENDANCE("Mark Attendance", "Attendance Management"),
    VIEW_ATTENDANCE("View Attendance", "Attendance Management"),

    // Department & System Authorities
    MANAGE_DEPARTMENT("Manage Department & Designation", "System Setup");

    private final String displayName;
    private final String category;

    Authority(String displayName, String category) {
        this.displayName = displayName;
        this.category = category;
    }
}
