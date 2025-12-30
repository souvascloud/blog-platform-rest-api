package com.souvanik.blog.user.service.impl;

import com.souvanik.blog.auth.security.util.SecurityUtil;
import com.souvanik.blog.common.exception.ErrorCode;
import com.souvanik.blog.common.exception.ResourceNotFoundException;
import com.souvanik.blog.user.dto.UpdateProfileRequest;
import com.souvanik.blog.user.dto.UserResponse;
import com.souvanik.blog.user.model.User;
import com.souvanik.blog.user.repository.UserRepository;
import com.souvanik.blog.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/*
 * Copyright (c) 2025 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);


    private final UserRepository userRepository;

    @PreAuthorize("isAuthenticated()")
    @Override
    public UserResponse getCurrentUser() {

        UUID userId = SecurityUtil.getCurrentUserId();
        logger.debug("Fetching current user profile for userId={}", userId);

        assert userId != null;
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.warn("Authenticated user not found in DB for userId={}", userId);
                    return new ResourceNotFoundException(
                            ErrorCode.RESOURCE_NOT_FOUND,
                            "Current user not found");
                });

        return toResponse(user);
    }

    @PreAuthorize("isAuthenticated()")
    @Override
    @Transactional
    public UserResponse updateCurrentUser(UpdateProfileRequest request) {

        UUID userId = SecurityUtil.getCurrentUserId();
        logger.debug("Updating current user profile for email={}", userId);

        assert userId != null;
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.warn("Authenticated user not found for update userId={}", userId);
                    return new ResourceNotFoundException(
                            ErrorCode.RESOURCE_NOT_FOUND,
                            "Current user not found");
                });

        user.setBio(request.getBio());

        logger.info("Updated profile for current user userId={}", userId);
        return toResponse(user);
    }


    private UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .bio(user.getBio())
                .role(user.getRole())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
