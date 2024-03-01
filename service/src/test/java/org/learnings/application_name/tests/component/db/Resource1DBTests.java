package org.learnings.application_name.tests.component.db;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.learnings.application_name.repositories.Resource1Entity;
import org.learnings.application_name.repositories.Resource1Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testing spring-data-jpa repositories
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("component-test")
public class Resource1DBTests {

    @Autowired
    private Resource1Repository repository;

    @BeforeEach
    void setup() {
        repository.deleteAll();
    }

    @Test
    void findAll_returnsEmptyList_whenEmpty() {
        List<Resource1Entity> findAll = repository.findAll();

        assertThat(findAll).isEmpty();
    }

    @Test
    void findAll_returnsResource_whenResourcesExist() {
        List<Resource1Entity> findAll = repository.findAll();
        assertThat(findAll).isEmpty();

        Resource1Entity entity1 = new Resource1Entity(1L, "attribute 1", "attribute 2");
        Resource1Entity entity2 = new Resource1Entity(2L, "attr 1", "attr 2");
        repository.save(entity1);
        repository.save(entity2);

        findAll = repository.findAll();
        assertThat(findAll).hasSize(2);
        assertThat(findAll).containsExactly(entity1, entity2);
    }

    @Test
    void getResource1ByID_returnsNothing_whenResourceDoesNotExist() {
        Optional<Resource1Entity> referenceById = repository.findById(123455431L);

        assertThat(referenceById).isEmpty();
    }

    @Test
    void getResource1ByID_returnsResource_whenResourceExists() {
        Resource1Entity entity3 = new Resource1Entity(3L, "attribute 1", "attribute 2");
        repository.save(entity3);

        Optional<Resource1Entity> referenceById = repository.findById(3L);

        assertThat(referenceById).isNotEmpty();
        assertThat(referenceById.get()).isEqualTo(entity3);
    }

    @Test
    void save_succeedsToStore_andToUpdate() {
        Resource1Entity entity4 = new Resource1Entity(4L, "attribute 1", "attribute 2");
        repository.save(entity4);

        Optional<Resource1Entity> referenceById = repository.findById(4L);
        assertThat(referenceById).isNotEmpty();
        assertThat(referenceById.get().getAttr1()).isEqualTo("attribute 1");

        Resource1Entity entity4b = new Resource1Entity(4L, "updated attribute 1", "attribute 2");
        repository.save(entity4b);

        referenceById = repository.findById(4L);
        assertThat(referenceById).isNotEmpty();
        assertThat(referenceById.get().getAttr1()).isEqualTo("updated attribute 1");
    }
}
