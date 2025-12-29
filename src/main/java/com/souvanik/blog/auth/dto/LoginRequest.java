package com.souvanik.blog.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

/*
 * Copyright (c) 2025 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
@Schema(description = "Request payload for user login")
@Getter
public class LoginRequest {

    @Schema(description = "Registered email address", example = "souvanik@example.com")
    private String email;

    @Schema(description = "Account password", example = "StrongPass@123")
    private String password;
}
