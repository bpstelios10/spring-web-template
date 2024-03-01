package org.learnings.application_name.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This creates endpoints using the name of the entity (in plural), like:
 * GET/POST /resource1Entities
 * GET /resource1Entities?page=1&size=10
 * GET/PUT/DELETE /resource1Entities/{id} -> if id is not found then a ResourceNotFound exception is thrown and is custom handled
 */
@Repository
public interface Resource1Repository extends JpaRepository<Resource1Entity, Long> {
}
