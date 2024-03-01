package org.learnings.application_name.repositories;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "resource2")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Resource2Entity {

    @Id
    private Long id;

    @Column(name = "attribute1")
    private String attr1;

    @Column(name = "attribute2")
    private String attr2;
}
