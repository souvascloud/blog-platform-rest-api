package com.souvanik.blog.common.api;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

/*
 * Copyright (c) 2025 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
@Getter
@Builder
public class ApiError {
    private final String code;
    private final String message;
    private final List<String> details;

}
