package com.souvanik.blog.post.service;

import com.souvanik.blog.post.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/*
 * Copyright (c) 2025 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
public interface PostService {
    PostResponse create(CreatePostRequest req);
    PostResponse update(UUID id, UpdatePostRequest req);
    void delete(UUID id);

    PostResponse getPublishedBySlug(String slug);
    Page<PostResponse> listPublished(Pageable pageable);

    CommentResponse addComment(UUID postId, CreateCommentRequest req);
    List<CommentResponse> listComments(UUID postId);

    void like(UUID postId);
    void unlike(UUID postId);
}
