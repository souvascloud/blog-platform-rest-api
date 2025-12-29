package com.souvanik.blog.auth.service;

import com.souvanik.blog.auth.dto.AuthResponse;
import com.souvanik.blog.auth.dto.LoginRequest;
import com.souvanik.blog.auth.dto.RefreshTokenRequest;
import com.souvanik.blog.auth.dto.RegisterRequest;

/*
 * Copyright (c) 2025 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
public interface AuthService {
    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse refresh(RefreshTokenRequest request);

    void logout(String refreshToken);

}
