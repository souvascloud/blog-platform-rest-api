package com.souvanik.blog.post.repository;

import com.souvanik.blog.post.model.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

/*
 * Copyright (c) 2025 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
public interface PostLikeRepository extends JpaRepository<PostLike, UUID> {
    boolean existsByPostIdAndUserId(UUID postId, UUID userId);
    void deleteByPostIdAndUserId(UUID postId, UUID userId);
    long countByPostId(UUID postId);
}