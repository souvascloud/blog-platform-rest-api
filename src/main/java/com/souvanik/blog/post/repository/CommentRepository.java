package com.souvanik.blog.post.repository;

import com.souvanik.blog.post.model.Comment;
import com.souvanik.blog.post.model.CommentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;


/*
 * Copyright (c) 2025 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
public interface CommentRepository extends JpaRepository<Comment, UUID> {
    List<Comment> findByPostIdAndStatus(UUID postId, CommentStatus status);
}
