package com.souvanik.blog.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

/*
 * Copyright (c) 2025 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
@Getter
public class RefreshTokenRequest {

    @NotBlank
    private String refreshToken;
}
