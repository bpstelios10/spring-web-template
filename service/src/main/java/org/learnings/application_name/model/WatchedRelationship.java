package org.learnings.application_name.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;

@RelationshipProperties
@Data
@NoArgsConstructor // Empty constructor required as of Neo4j API 2.0.5
@AllArgsConstructor
public final class WatchedRelationship {

    @RelationshipId
    @GeneratedValue
    private Long id;

    @TargetNode
    private Movie movie;

    private short rating;
}
