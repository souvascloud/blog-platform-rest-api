package com.souvanik.blog.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.Getter;

/*
 * Copyright (c) 2025 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
@Schema(description = "Request payload to update current user profile")
@Getter
public class UpdateProfileRequest {

    @Schema(description = "Short bio", example = "Senior backend developer")
    @Size(max = 500, message = "Bio can be at most 500 characters")
    private String bio;
}
