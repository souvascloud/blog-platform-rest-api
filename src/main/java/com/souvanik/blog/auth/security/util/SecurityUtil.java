package com.souvanik.blog.auth.security.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

/*
 * Copyright (c) 2025 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
public class SecurityUtil{

    private SecurityUtil() {}
    // Dummy bcrypt hash to prevent timing attacks
    public static  final String DUMMY_PASSWORD_HASH = "$2a$10$7EqJtq98hPqEX7fNZaFWoOHiZqJH8k3yN2tV1p7Cq9Gm6XjYy";
    public static CustomUserPrincipal getCurrentUserPrincipal() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }

        Object principal = auth.getPrincipal();
        if (principal instanceof CustomUserPrincipal) {
            return (CustomUserPrincipal) principal;
        }

        return null;
    }

    public static UUID getCurrentUserId() {
        CustomUserPrincipal principal = getCurrentUserPrincipal();
        return principal != null ? principal.getUserId() : null;
    }

    public static String getCurrentUserEmail() {
        CustomUserPrincipal principal = getCurrentUserPrincipal();
        return principal != null ? principal.getEmail() : null;
    }
}
