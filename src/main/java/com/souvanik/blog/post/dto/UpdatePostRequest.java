package com.souvanik.blog.post.dto;

import com.souvanik.blog.post.model.PostStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.Set;

/*
 * Copyright (c) 2025 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
@Schema(description = "Request payload to update an existing blog post")
@Getter
public class UpdatePostRequest {
    @Schema(
            description = "Updated title of the post (max 200 characters)",
            example = "Spring Boot REST API Best Practices – Updated"
    )
    @Size(max=200) private String title;


    @Schema(
            description = "Updated content of the post",
            example = "This post has been updated with additional best practices and examples."
    )
    private String content;

    @Schema(
            description = "Updated tags for the post (each tag max 50 characters)",
            example = "[\"spring\", \"java\", \"api\"]"
    )
    private Set<@Size(max=50) String> tags;

    @Schema(
            description = "Updated status of the post",
            example = "DRAFT"
    )
    private PostStatus status;
}