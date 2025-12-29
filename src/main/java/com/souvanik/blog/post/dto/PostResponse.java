package com.souvanik.blog.post.dto;

import com.souvanik.blog.post.model.PostStatus;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Blog post response")
@Getter
@Builder
public class PostResponse {
    @Schema(example = "b1b2c3d4-1234-5678-9999-acde12345678")
    private UUID id;

    @Schema(example = "Spring Boot REST API Best Practices")
    private String title;

    @Schema(example = "spring-boot-rest-api-best-practices")
    private String slug;

    @Schema(example = "In this post we explore REST API design...")
    private String content;

    @Schema(example = "PUBLISHED")
    private PostStatus status;

    @Schema(example = "souvanik")
    private String author;

    @Schema(example = "[\"spring\",\"java\",\"backend\"]")
    private Set<String> tags;

    @Schema(example = "10")
    private long likes;

    @Schema(example = "2025-01-01T10:59:30Z")
    private Instant createdAt;
}

