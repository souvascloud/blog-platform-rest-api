package com.souvanik.blog.post.model;

import com.souvanik.blog.user.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

/*
 * Copyright (c) 2025 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
@Entity
@Table(name="comments")
@Getter
@Setter
public class Comment {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    @JoinColumn(name="post_id") private Post post;

    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    @JoinColumn(name="author_id") private User author;

    @Lob @Column(nullable=false) private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable=false, length=20)
    private CommentStatus status;

    @CreationTimestamp
    @Column(updatable=false) private Instant createdAt;
}