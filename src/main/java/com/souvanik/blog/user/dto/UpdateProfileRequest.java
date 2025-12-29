package com.souvanik.blog.user.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;

/*
 * Copyright (c) 2025 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
@Getter
public class UpdateProfileRequest {

    @Size(max = 500, message = "Bio can be at most 500 characters")
    private String bio;
}