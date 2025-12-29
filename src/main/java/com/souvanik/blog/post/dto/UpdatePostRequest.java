package com.souvanik.blog.post.dto;

import com.souvanik.blog.post.model.PostStatus;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.Set;

/*
 * Copyright (c) 2025 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
@Getter
public class UpdatePostRequest {
    @Size(max=200) private String title;
    private String content;
    private Set<@Size(max=50) String> tags;
    private PostStatus status;
}
