package com.souvanik.blog.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

/*
 * Copyright (c) 2025 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
@Schema(description = "Request payload containing refresh token")
@Getter
public class RefreshTokenRequest {

    @Schema(description = "Valid refresh token", example = "b7a3c2f1-2e8a-4d4e-9d77-3a2f1d8e9b10")
    @NotBlank
    private String refreshToken;
}