package com.souvanik.blog.admin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

/*
 * Copyright (c) 2025 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
@Schema(description = "Administrative platform statistics response")
@Getter
@Builder
public class AdminStatsResponse {
    @Schema(description = "Total number of registered users", example = "1200")
    private long totalUsers;

    @Schema(description = "Total number of posts", example = "4500")
    private long totalPosts;

    @Schema(description = "Total number of comments", example = "18200")
    private long totalComments;

    @Schema(description = "Total number of likes across all posts", example = "56000")
    private long totalLikes;
}

