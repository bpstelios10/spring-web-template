package org.learnings.application_name.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node
@Data
@NoArgsConstructor // Empty constructor required as of Neo4j API 2.0.5
@AllArgsConstructor
public class Person {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private int born;

    public Person(String name, int born) {
        this.name = name;
        this.born = born;
    }
}
