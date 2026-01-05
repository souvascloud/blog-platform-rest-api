package com.souvanik.blog.auth.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

/*
 * Copyright (c) 2026 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
@Component
public class CookieService {

    private static final String REFRESH_TOKEN_COOKIE = "refresh_token";

    /**
     * Extracts refresh token from HttpOnly cookie.
     */
    public String extract(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }

        for (Cookie cookie : request.getCookies()) {
            if (REFRESH_TOKEN_COOKIE.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }

    /**
     * Sets refresh token as secure HttpOnly cookie.
     */
    public void setRefreshToken(HttpServletResponse response,
                                String token,
                                Instant expiry) {

        ResponseCookie cookie = ResponseCookie.from(
                        REFRESH_TOKEN_COOKIE, token)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/auth")
                .maxAge(Duration.between(Instant.now(), expiry))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    /**
     * Clears refresh token cookie (logout).
     */
    public void clearRefreshToken(HttpServletResponse response) {

        ResponseCookie cookie = ResponseCookie.from(
                        REFRESH_TOKEN_COOKIE, "")
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/auth")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
}
