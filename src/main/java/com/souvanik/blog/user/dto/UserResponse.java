package com.souvanik.blog.user.dto;

import com.souvanik.blog.user.model.Role;
import com.souvanik.blog.user.model.UserStatus;
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
@Schema(description = "User profile response")
@Getter
@Builder
public class UserResponse {
    @Schema(description = "User unique identifier",
            example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID id;

    @Schema(description = "Username", example = "souvanik")
    private String username;

    @Schema(description = "Email address", example = "souvanik@example.com")
    private String email;

    @Schema(description = "Short bio", example = "Senior backend developer")
    private String bio;

    @Schema(description = "User role", example = "USER")
    private Role role;

    @Schema(description = "Account status", example = "ACTIVE")
    private UserStatus status;

    @Schema(description = "Account creation time",
            example = "2025-01-01T10:15:30Z")
    private Instant createdAt;
}
