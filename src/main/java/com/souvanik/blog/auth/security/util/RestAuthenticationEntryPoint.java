package com.souvanik.blog.auth.security.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.souvanik.blog.common.api.ApiError;
import com.souvanik.blog.common.api.ApiResponse;
import com.souvanik.blog.common.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/*
 * Copyright (c) 2025 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public RestAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException {

        ApiResponse<Void> body =
                ApiResponse.error(
                        401,
                        ApiError.builder()
                                .code(ErrorCode.UNAUTHORIZED.name())
                                .message("Authentication required")
                                .build()
                );

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        objectMapper.writeValue(response.getOutputStream(), body);
    }
}
