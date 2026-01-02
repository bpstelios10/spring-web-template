package org.learnings.application_name.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.*;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.data.neo4j.core.schema.Relationship.Direction.INCOMING;

@Node
@Data
@NoArgsConstructor // Empty constructor required as of Neo4j API 2.0.5
@AllArgsConstructor
public class Movie {

    @Id
    @GeneratedValue
    private Long id;
    private String title;
    @Property("tagline")
    private String description;
    @Relationship(type = "ACTED_IN", direction = INCOMING)
    private Set<Person> actors = new HashSet<>();
    @Relationship(type = "DIRECTED", direction = INCOMING)
    private Set<Person> directors = new HashSet<>();

    public Movie(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
