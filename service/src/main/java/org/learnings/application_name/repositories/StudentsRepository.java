package org.learnings.application_name.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StudentsRepository extends JpaRepository<StudentEntity, UUID> {

    @Query("SELECT s FROM StudentEntity s WHERE s.fullName LIKE %:name%")
    List<StudentEntity> findByFullName(String name);
}
