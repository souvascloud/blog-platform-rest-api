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
@Table(name="post_likes",
        uniqueConstraints = @UniqueConstraint(name="uk_post_user", columnNames={"post_id","user_id"}))
@Getter
@Setter
public class PostLike {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    @JoinColumn(name="post_id") private Post post;

    @ManyToOne(fetch=FetchType.LAZY, optional=false)
    @JoinColumn(name="user_id") private User user;

    @CreationTimestamp
    @Column(updatable=false) private Instant createdAt;
}