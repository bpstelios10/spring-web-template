package org.learnings.application_name.repositories;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "resource1")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Resource3Entity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "attribute1")
    private String attr1;

    @Column(name = "attribute2")
    private String attr2;
}
