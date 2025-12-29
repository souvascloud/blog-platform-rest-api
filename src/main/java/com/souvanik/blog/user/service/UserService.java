package com.souvanik.blog.user.service;

import com.souvanik.blog.user.dto.UpdateProfileRequest;
import com.souvanik.blog.user.dto.UserResponse;

import java.util.UUID;

/*
 * Copyright (c) 2025 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
public interface UserService {
    UserResponse getCurrentUser();

    UserResponse updateCurrentUser(UpdateProfileRequest request);
}
