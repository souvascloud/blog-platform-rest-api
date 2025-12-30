package com.souvanik.blog.common.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

/*
 * Copyright (c) 2025 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
@Schema(description = "Standard error response")
@Getter
@Builder
public class ApiError {


    @Schema(example = "RESOURCE_NOT_FOUND")
    private String code;

    @Schema(example = "User not found")
    private String message;


    @Schema(example = "2025-01-01T10:15:30Z",
            description = "Time when the error occurred in UTC")
    private final Instant timestamp = Instant.now();


    @Schema(description = "Additional error details",
            example = "[\"email must not be blank\", \"password must be at least 8 characters\"]")
    private final List<String> details;

}
