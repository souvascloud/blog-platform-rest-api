package com.souvanik.blog.auth.controller;

import com.souvanik.blog.auth.dto.AuthResponse;
import com.souvanik.blog.auth.dto.LoginRequest;
import com.souvanik.blog.auth.dto.RefreshTokenRequest;
import com.souvanik.blog.auth.dto.RegisterRequest;
import com.souvanik.blog.auth.security.util.CustomUserPrincipal;
import com.souvanik.blog.auth.service.AuthService;
import com.souvanik.blog.common.SwaggerExamples;
import com.souvanik.blog.common.api.ApiResponse;
import com.souvanik.blog.common.config.OpenApiConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * Copyright (c) 2025 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 *
 * Authentication Controller
 *
 * Provides APIs for:
 * - User registration
 * - User login
 * - JWT access token refresh
 * - Logout by revoking refresh tokens
 *
 * All APIs return a standard ApiResponse<T> wrapper
 * for consistent response structure across the system.
 *
 * Base URL: /api/v1/auth
 */
@Tag(
        name = "Authentication",
        description = "APIs for user registration, login, and JWT token management"
)
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }




    /**
     * Registers a new user in the system.
     *
     * Flow:
     * 1. Validates registration payload
     * 2. Creates user with USER role
     * 3. Hashes password securely
     * 4. Issues JWT access & refresh tokens
     *
     * @param request registration details
     * @return JWT access and refresh tokens
     */
    @Operation(summary = "Register a new user",
            description = """
              Creates a new user account with USER role.On success, returns JWT access and refresh tokens.
              """,
            security = {}
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "User registered successfully",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = SwaggerExamples.AUTH_SUCCESS
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Validation error",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = SwaggerExamples.VALIDATION_ERROR
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "Email or username already exists",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = SwaggerExamples.CONFLICT
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = SwaggerExamples.INTERNAL_SERVER_ERROR
                            )
                    )
            )
    })
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Registration payload",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = RegisterRequest.class),
                            examples = @ExampleObject(value = """
                        {
                          "username": "souvanik",
                          "email": "souvanik@example.com",
                          "password": "StrongPass@123"
                        }
                        """)
                    )
            )
            @RequestBody @Valid RegisterRequest request) {

        logger.debug("POST /auth/register for email={}", request.getEmail());

        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(ApiResponse.success(200, response));
    }



    /**
     * Authenticates a user and issues JWT tokens.
     */

    @Operation(
            summary = "Login user",
            description = "Authenticates user and returns JWT access and refresh tokens.",
            security = {}
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Login successful",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = SwaggerExamples.AUTH_SUCCESS
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Validation error",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = SwaggerExamples.VALIDATION_ERROR
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Invalid credentials",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = SwaggerExamples.UNAUTHORIZED
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = SwaggerExamples.INTERNAL_SERVER_ERROR
                            )
                    )
            )
    })
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Login credentials",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = LoginRequest.class),
                            examples = @ExampleObject(value = """
            {
              "email": "souvanik@example.com",
              "password": "StrongPass@123"
            }
            """)
                    )
            )
            @RequestBody @Valid LoginRequest request) {

        logger.debug("POST /auth/login for email={}", request.getEmail());

        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success(200, response));
    }



    /**
     * Refreshes JWT access token using a refresh token.
     */
    @Operation(
            summary = "Refresh access token",
            description = "Generates a new access token using a valid refresh token.",
            security = {}
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Token refreshed successfully",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = SwaggerExamples.AUTH_SUCCESS
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Invalid or expired refresh token",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = SwaggerExamples.TOKEN_EXPIRED
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = SwaggerExamples.INTERNAL_SERVER_ERROR
                            )
                    )
            )
    })
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Refresh token payload",
                    required = true,
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            schema = @io.swagger.v3.oas.annotations.media.Schema(
                                    implementation = RefreshTokenRequest.class
                            ),
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = SwaggerExamples.REFRESH_TOKEN_REQUEST
                            )
                    )
            )
            @RequestBody @Valid RefreshTokenRequest request) {

        logger.debug("POST /auth/refresh");

        AuthResponse response = authService.refresh(request);
        return ResponseEntity.ok(ApiResponse.success(200, response));
    }


    /**
     * Logs out the user by revoking the refresh token.
     */

    @Operation(
            summary = "Logout user",
            description = "Revokes the refresh token so it can no longer be used.Requires JWT access token",
            security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(
                    name = OpenApiConfig.SECURITY_SCHEME_NAME
            )
    )
    @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth")
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Logout successful",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = SwaggerExamples.LOGOUT_SUCCESS
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Invalid or expired refresh token",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = SwaggerExamples.LOGOUT_UNAUTHORIZED
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "403",
                    description = "User blocked",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = SwaggerExamples.LOGOUT_FORBIDDEN
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            mediaType = "application/json",
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = SwaggerExamples.INTERNAL_SERVER_ERROR
                            )
                    )
            )
    })
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Refresh token to revoke",
                    required = true,
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            schema = @io.swagger.v3.oas.annotations.media.Schema(
                                    implementation = RefreshTokenRequest.class
                            ),
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = SwaggerExamples.REFRESH_TOKEN_REQUEST
                            )
                    )
            )
            @RequestBody @Valid RefreshTokenRequest request) {

        logger.debug("POST /auth/logout");

        authService.logout(request.getRefreshToken());
        return ResponseEntity.ok(ApiResponse.success(200, null));
    }




    @io.swagger.v3.oas.annotations.Operation(
            summary = "Logout from all devices",
            description = "Revokes all refresh tokens for the currently authenticated user.Requires JWT access token ",
            security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(
                    name = OpenApiConfig.SECURITY_SCHEME_NAME
            )
    )
    @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth")
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Logged out from all devices",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = SwaggerExamples.LOGOUT_ALL_SUCCESS
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = SwaggerExamples.UNAUTHORIZED
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = SwaggerExamples.INTERNAL_SERVER_ERROR
                            )
                    )
            )
    })
    @PostMapping("/logout-all")
    public ResponseEntity<ApiResponse<Void>> logoutAll(
            @AuthenticationPrincipal CustomUserPrincipal principal
    ) {

        logger.debug("POST /auth/logout-all for userId={}", principal.getUserId());
        authService.logoutAll(principal.getUserId());

        return ResponseEntity.ok(ApiResponse.success(200, null));
    }
}