package com.souvanik.blog.user.service.impl;

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

    @Override
    public UserResponse getById(UUID id) {
        logger.debug("Fetching user by id={}", id);

        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        "User not found with id: " + id));

        logger.info("Fetched user id={} username={}", user.getId(), user.getUsername());
        return toResponse(user);
    }

    @Override
    @Transactional
    public UserResponse updateProfile(UUID userId, UpdateProfileRequest request) {
        logger.debug("Updating profile for userId={}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        "User not found with id: " + userId));

        user.setBio(request.getBio());

        logger.info("Updated profile for userId={}", userId);
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
