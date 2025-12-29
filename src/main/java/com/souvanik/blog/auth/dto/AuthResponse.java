package com.souvanik.blog.auth.dto;

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
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType; // Bearer
    private long expiresIn;   // seconds
}
