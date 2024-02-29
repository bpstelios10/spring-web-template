package org.learnings.application_name.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * This repo does not export endpoints at all
 */
@RepositoryRestResource(exported = false)
public interface Resource3Repository extends JpaRepository<Resource3Entity, Long> {
}
