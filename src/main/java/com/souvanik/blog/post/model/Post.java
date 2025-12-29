package com.souvanik.blog.post.model;

import com.souvanik.blog.user.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/*
 * Copyright (c) 2025 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
@Entity
@Table(name="posts",
        indexes = {
                @Index(name="idx_posts_slug", columnList="slug", unique=true),
                @Index(name="idx_posts_status", columnList="status")
        })
@Getter
@Setter
public class Post {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional=false)
    @JoinColumn(name="author_id", nullable=false)
    private User author;

    @Column(nullable=false, length=200) private String title;
    @Column(nullable=false, length=220) private String slug;

    @Column(columnDefinition = "TEXT", nullable = false) private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=20)
    private PostStatus status;

    @ManyToMany
    @JoinTable(name="post_tags",
            joinColumns=@JoinColumn(name="post_id"),
            inverseJoinColumns=@JoinColumn(name="tag_id"))
    private Set<Tag> tags = new HashSet<>();

    @CreationTimestamp
    @Column(updatable=false) private Instant createdAt;
    @UpdateTimestamp
    private Instant updatedAt;
}