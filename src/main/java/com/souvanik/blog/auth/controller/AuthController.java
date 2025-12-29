package com.souvanik.blog.auth.controller;

import com.souvanik.blog.auth.dto.AuthResponse;
import com.souvanik.blog.auth.dto.LoginRequest;
import com.souvanik.blog.auth.dto.RefreshTokenRequest;
import com.souvanik.blog.auth.dto.RegisterRequest;
import com.souvanik.blog.auth.service.AuthService;
import com.souvanik.blog.common.api.ApiResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * Copyright (c) 2025 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @RequestBody @Valid RegisterRequest request) {

        logger.debug("POST /auth/register for email={}", request.getEmail());

        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(ApiResponse.success(200, response));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @RequestBody @Valid LoginRequest request) {

        logger.debug("POST /auth/login for email={}", request.getEmail());

        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success(200, response));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(
            @RequestBody @Valid RefreshTokenRequest request) {

        logger.debug("POST /auth/refresh");

        AuthResponse response = authService.refresh(request);
        return ResponseEntity.ok(ApiResponse.success(200, response));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @RequestBody @Valid RefreshTokenRequest request) {

        logger.debug("POST /auth/logout");

        authService.logout(request.getRefreshToken());
        return ResponseEntity.ok(ApiResponse.success(200, null));
    }
}