package com.schoolsaathi.school_managment.repository;



import com.schoolsaathi.school_managment.entity.User;
import com.schoolsaathi.school_managment.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository
        extends JpaRepository<User, UUID> {

    // ─────────────────────────────────────
    // Basic Finders
    // ─────────────────────────────────────

    Optional<User> findByEmailAndIsDeletedFalse(
            String email
    );

    Optional<User> findByIdAndSchoolIdAndIsDeletedFalse(
            UUID id, UUID schoolId
    );

    Optional<User> findByIdAndIsDeletedFalse(
            UUID id
    );

    // ─────────────────────────────────────
    // Existence Checks
    // ─────────────────────────────────────

    Boolean existsByEmailAndSchoolIdAndIsDeletedFalse(
            String email, UUID schoolId
    );

    Boolean existsByEmailAndIsDeletedFalse(
            String email
    );

    // ─────────────────────────────────────
    // List Queries
    // ─────────────────────────────────────

    // School ke saare users
    List<User> findAllBySchoolIdAndIsDeletedFalse(
            UUID schoolId
    );

    // Role ke hisaab se users
    List<User> findAllBySchoolIdAndRoleAndIsDeletedFalse(
            UUID schoolId, UserRole role
    );

    // Active users only
    List<User> findAllBySchoolIdAndIsActiveTrueAndIsDeletedFalse(
            UUID schoolId
    );

    // Active teachers only
    List<User> findAllBySchoolIdAndRoleAndIsActiveTrueAndIsDeletedFalse(
            UUID schoolId, UserRole role
    );

    // ─────────────────────────────────────
    // Count Queries
    // ─────────────────────────────────────

    Long countBySchoolIdAndIsDeletedFalse(
            UUID schoolId
    );

    Long countBySchoolIdAndRoleAndIsDeletedFalse(
            UUID schoolId, UserRole role
    );

    // ─────────────────────────────────────
    // Search
    // ─────────────────────────────────────

    @Query("""
            SELECT u FROM User u
            WHERE u.schoolId = :schoolId
            AND u.isDeleted = false
            AND (
                LOWER(u.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(u.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(u.phone) LIKE LOWER(CONCAT('%', :keyword, '%'))
            )
            """)
    List<User> searchUsers(
            @Param("schoolId") UUID schoolId,
            @Param("keyword") String keyword
    );
}