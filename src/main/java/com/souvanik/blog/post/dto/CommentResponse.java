package com.souvanik.blog.post.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

/*
 * Copyright (c) 2025 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
@Getter
@Builder
public class CommentResponse {

    private UUID id;
    private String author;
    private String content;
    private Instant createdAt;
}
