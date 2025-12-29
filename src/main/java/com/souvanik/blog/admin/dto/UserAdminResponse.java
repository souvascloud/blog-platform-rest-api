package com.souvanik.blog.admin.dto;

import com.souvanik.blog.user.model.Role;
import com.souvanik.blog.user.model.UserStatus;
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
public class UserAdminResponse {
    private UUID id;
    private String username;
    private String email;
    private Role role;
    private UserStatus status;
    private Instant createdAt;
}
