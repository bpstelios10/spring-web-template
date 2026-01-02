package org.learnings.application_name.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * This creates endpoints using the name of the entity (in plural), like:
 * GET/POST /resource2
 * GET /resource2?page=1&size=10
 * GET/PUT/DELETE /resource2/{id} -> if id is not found then a ResourceNotFound exception is thrown and is custom handled
 */
@RepositoryRestResource(path = "resource2")
public interface Resource2Repository extends JpaRepository<Resource2Entity, Long> {
}
