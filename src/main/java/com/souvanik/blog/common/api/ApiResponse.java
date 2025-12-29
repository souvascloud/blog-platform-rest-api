package com.souvanik.blog.common.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

/*
 * Copyright (c) 2025 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
@Schema(description = "Standard API response wrapper")
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    @Schema(example = "2025-01-01T10:15:30Z")
    private final Instant timestamp;

    @Schema(example = "200")
    private final int status;

    @Schema(description = "true if request was successful")
    private final boolean success;

    @Schema(description = "Actual response payload(Present Only on Success)")
    private final T data;

    @Schema(description = "Error details if request failed")
    private final ApiError error;

    @Schema(description = "Pagination metadata (if applicable)")
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
