package com.souvanik.blog.post.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/*
 * Copyright (c) 2025 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
@Entity
@Table(name = "tags", uniqueConstraints = @UniqueConstraint(name="uk_tags_name", columnNames="name"))
@Getter
@Setter
public class Tag {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(nullable=false, length=50) private String name;
}
