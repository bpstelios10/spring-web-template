package org.learnings.application_name.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StudentsRepository extends JpaRepository<StudentsEntity, UUID> {

    @Query("SELECT s FROM StudentsEntity s WHERE s.fullName LIKE %:name%")
    List<StudentsEntity> findByFullName(String name);
}
