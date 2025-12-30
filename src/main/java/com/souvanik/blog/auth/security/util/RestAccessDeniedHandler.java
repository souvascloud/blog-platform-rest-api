package com.souvanik.blog.auth.security.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.souvanik.blog.common.api.ApiError;
import com.souvanik.blog.common.api.ApiResponse;
import com.souvanik.blog.common.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/*
 * Copyright (c) 2025 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
@Component
public class RestAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    public RestAccessDeniedHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException ex
    ) throws IOException {
        ApiResponse<Void> body =
                ApiResponse.error(
                        403,
                        ApiError.builder()
                                .code(ErrorCode.FORBIDDEN.name())
                                .message("You do not have permission to perform this action")
                                .build()
                );

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        objectMapper.writeValue(response.getOutputStream(), body);
    }
}
