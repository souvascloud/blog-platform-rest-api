package com.souvanik.blog.user.controller;

import com.souvanik.blog.common.api.ApiResponse;
import com.souvanik.blog.user.dto.UpdateProfileRequest;
import com.souvanik.blog.user.dto.UserResponse;
import com.souvanik.blog.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/*
 * Copyright (c) 2025 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getMe() {
        logger.debug("GET /api/v1/users/me");

        UserResponse user = userService.getCurrentUser();
        return ResponseEntity.ok(ApiResponse.success(200, user));
    }

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> updateMe(
            @RequestBody @Valid UpdateProfileRequest request) {

        logger.debug("PUT /api/v1/users/me - update profile");

        UserResponse updated = userService.updateCurrentUser(request);
        return ResponseEntity.ok(ApiResponse.success(200, updated));
    }
}
