package com.souvanik.blog.post.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.souvanik.blog.post.model.Post;
import com.souvanik.blog.post.model.PostStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

/*
 * Copyright (c) 2025 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
public interface PostRepository extends JpaRepository<Post, UUID> {
    Optional<Post> findBySlugAndStatus(String slug, PostStatus status);
    Page<Post> findByStatus(PostStatus status, Pageable pageable);
    boolean existsBySlug(String slug);
}
