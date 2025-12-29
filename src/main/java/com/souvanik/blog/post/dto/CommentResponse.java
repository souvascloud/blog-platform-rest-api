package com.souvanik.blog.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Comment response")
@Getter
@Builder
public class CommentResponse {

    @Schema(
            description = "Unique identifier of the comment",
            example = "c1234567-89ab-4def-9012-abcdef123456"
    )
    private UUID id;

    @Schema(
            description = "Username of the commenter",
            example = "john_doe"
    )
    private String author;

    @Schema(
            description = "Comment text",
            example = "This post explained REST API design really well!"
    )
    private String content;

    @Schema(
            description = "Comment creation timestamp",
            example = "2025-01-01T11:05:00Z"
    )
    private Instant createdAt;
}
