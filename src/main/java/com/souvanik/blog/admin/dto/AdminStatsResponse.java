package com.souvanik.blog.admin.dto;

import lombok.Builder;
import lombok.Getter;

/*
 * Copyright (c) 2025 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
@Getter
@Builder
public class AdminStatsResponse {
    private long totalUsers;
    private long totalPosts;
    private long totalComments;
    private long totalLikes;
}
