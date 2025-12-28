package com.souvanik.blog.common.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

/*
 * Copyright (c) 2025 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private final Instant timestamp;
    private final int status;
    private final boolean success;
    private final T data;
    private final ApiError error;
    private final Object meta;

    public static <T> ApiResponse<T> success(int status, T data, Object meta) {
        return ApiResponse.<T>builder()
                .timestamp(Instant.now())
                .status(status)
                .success(true)
                .data(data)
                .meta(meta)
                .build();
    }

    public static <T> ApiResponse<T> success(int status, T data) {
        return success(status, data, null);
    }

    public static ApiResponse<Void> error(int status, ApiError error) {
        return ApiResponse.<Void>builder()
                .timestamp(Instant.now())
                .status(status)
                .success(false)
                .error(error)
                .build();
    }
}
