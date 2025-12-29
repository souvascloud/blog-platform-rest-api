package com.souvanik.blog.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

/*
 * Copyright (c) 2025 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
@Schema(description = "Request payload to add a comment to a post")
@Getter
public class CreateCommentRequest {
    @Schema(
            description = "Comment content",
            example = "This post explained REST API design really well!"
    )
    @NotBlank
    private String content;
}


