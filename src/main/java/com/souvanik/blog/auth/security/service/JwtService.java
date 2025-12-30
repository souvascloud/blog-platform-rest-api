package com.souvanik.blog.auth.security.service;

import com.souvanik.blog.user.model.User;

/*
 * Copyright (c) 2025 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
public interface JwtService {

    String generateAccessToken(User user);

    String generateRefreshToken();

    String extractUsername(String token);

    boolean isTokenValid(String token, User user);
}
