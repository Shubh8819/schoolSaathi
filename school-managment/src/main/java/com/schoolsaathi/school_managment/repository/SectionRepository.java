package com.schoolsaathi.school_managment.repository;

import com.schoolsaathi.school_managment.entity.Section;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SectionRepository extends JpaRepository<Section,UUID> {
    Optional<Section> findByIdAndSchoolIdAndIsDeletedFalse(@NotNull(message = "Section required") UUID sectionId, UUID schoolId);
    Optional<List<Section>> findBySchoolIdAndClassRoomIdAndIsDeletedFalse(UUID schoolId,UUID classRoomId);
}
