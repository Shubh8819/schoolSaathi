package com.schoolsaathi.school_managment.repository;

import com.schoolsaathi.school_managment.entity.ClassRoom;
import com.schoolsaathi.school_managment.entity.Student;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClassRoomRepository  extends JpaRepository<ClassRoom, UUID> {
    Optional <ClassRoom > findByIdAndSchoolIdAndIsDeletedFalse(@NotNull(message = "Class required") UUID classId, UUID schoolId);

    Optional <List<ClassRoom> > findAllBySchoolIdAndIsDeletedFalse(UUID schoolId);
}
