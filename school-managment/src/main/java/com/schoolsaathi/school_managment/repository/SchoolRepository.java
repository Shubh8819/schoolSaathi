package com.schoolsaathi.school_managment.repository;


import com.schoolsaathi.school_managment.entity.School;
import com.schoolsaathi.school_managment.enums.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SchoolRepository
        extends JpaRepository<School, UUID> {

    // ─────────────────────────────────────
    // Basic Finders
    // ─────────────────────────────────────

    Optional<School> findByIdAndIsDeletedFalse(
            UUID id
    );

    Optional<School> findByEmailAndIsDeletedFalse(
            String email
    );

    Optional<School> findBySchoolCodeAndIsDeletedFalse(
            String schoolCode
    );

    // ─────────────────────────────────────
    // Existence Checks
    // ─────────────────────────────────────

    Boolean existsByEmailAndIsDeletedFalse(
            String email
    );

    Boolean existsBySchoolCodeAndIsDeletedFalse(
            String schoolCode
    );

    // ─────────────────────────────────────
    // List Queries
    // ─────────────────────────────────────

    List<School> findAllByIsDeletedFalse();

    List<School> findAllByIsActiveTrueAndIsDeletedFalse();

    List<School> findAllByIsTrialTrueAndIsDeletedFalse();

    List<School> findAllBySubscriptionPlanAndIsDeletedFalse(
            SubscriptionPlan plan
    );

    // City ke hisaab se schools
    List<School> findAllByCityAndIsDeletedFalse(
            String city
    );

    // ─────────────────────────────────────
    // Subscription Queries
    // ─────────────────────────────────────

    // Trial expire hone wale schools
    @Query("""
            SELECT s FROM School s
            WHERE s.isTrial = true
            AND s.trialEndDate <= :date
            AND s.isActive = true
            AND s.isDeleted = false
            """)
    List<School> findExpiringTrials(
            @Param("date") LocalDate date
    );

    // Subscription expire hone wale schools
    @Query("""
            SELECT s FROM School s
            WHERE s.isTrial = false
            AND s.subscriptionEnd <= :date
            AND s.isActive = true
            AND s.isDeleted = false
            """)
    List<School> findExpiringSubscriptions(
            @Param("date") LocalDate date
    );

    // Agle X din mein expire hone wale
    @Query("""
            SELECT s FROM School s
            WHERE s.subscriptionEnd 
                BETWEEN :today AND :futureDate
            AND s.isActive = true
            AND s.isDeleted = false
            """)
    List<School> findSubscriptionsExpiringSoon(
            @Param("today") LocalDate today,
            @Param("futureDate") LocalDate futureDate
    );

    // ─────────────────────────────────────
    // Dashboard / Count Queries
    // ─────────────────────────────────────

    // Total active schools count
    Long countByIsActiveTrueAndIsDeletedFalse();

    // Trial schools count
    Long countByIsTrialTrueAndIsDeletedFalse();

    // Plan ke hisaab se count
    Long countBySubscriptionPlanAndIsDeletedFalse(
            SubscriptionPlan plan
    );

    // Is month mein kitne naye schools aaye
    @Query("""
            SELECT COUNT(s) FROM School s
            WHERE MONTH(s.createdAt) = MONTH(:date)
            AND YEAR(s.createdAt) = YEAR(:date)
            AND s.isDeleted = false
            """)
    Long countNewSchoolsThisMonth(
            @Param("date") LocalDate date
    );

    // ─────────────────────────────────────
    // Search Query
    // ─────────────────────────────────────

    @Query("""
            SELECT s FROM School s
            WHERE s.isDeleted = false
            AND (
                LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(s.city) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(s.schoolCode) LIKE LOWER(CONCAT('%', :keyword, '%'))
                OR LOWER(s.principalName) LIKE LOWER(CONCAT('%', :keyword, '%'))
            )
            """)
    List<School> searchSchools(
            @Param("keyword") String keyword
    );
}