package com.souvanik.blog.admin.dto;

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
@Getter
@Builder
@Schema(description = "User details for administrative view")
public class UserAdminResponse {
    @Schema(description = "User unique identifier",
           example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    private UUID id;

    @Schema(description = "Username", example = "souvanik")
    private String username;

    @Schema(description = "Email address", example = "souvanik@example.com")
    private String email;

    @Schema(description = "User role", example = "USER")
    private Role role;

    @Schema(description = "User account status", example = "ACTIVE")
    private UserStatus status;


    @Schema(description = "Account creation timestamp",
            example = "2024-12-01T08:30:00Z")
    private Instant createdAt;
}