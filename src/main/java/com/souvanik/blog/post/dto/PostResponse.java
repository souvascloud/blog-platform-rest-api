package com.souvanik.blog.post.dto;

import com.souvanik.blog.post.model.PostStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

/*
 * Copyright (c) 2025 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
@Getter
@Builder
public class PostResponse {
    private UUID id;
    private String title;
    private String slug;
    private String content;
    private PostStatus status;
    private String author;
    private Set<String> tags;
    private long likes;
    private Instant createdAt;
}
