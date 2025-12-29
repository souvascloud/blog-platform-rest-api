package com.souvanik.blog.post.dto;

import com.souvanik.blog.post.model.PostStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.Set;

/*
 * Copyright (c) 2025 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
@Schema(description = "Request payload to create a new blog post")
@Getter
public class CreatePostRequest {
    @Schema(
            description = "Title of the blog post",
            example = "Spring Boot REST API Best Practices"
    )
    @NotBlank
    @Size(max=200) private String title;

    @Schema(
            description = "Main content of the blog post",
            example = "In this post we explore REST API design..."
    )
    @NotBlank private String content;

    @Schema(
            description = "Tags associated with the post",
            example = "[\"spring\", \"java\", \"backend\"]"
    )
    private Set<@Size(max=50) String> tags;

    @Schema(
            description = "Initial status of the post",
            example = "PUBLISHED"
    )
    private PostStatus status;
}
