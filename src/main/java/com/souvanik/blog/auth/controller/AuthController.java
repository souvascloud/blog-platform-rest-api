package com.souvanik.blog.auth.controller;

import com.souvanik.blog.auth.dto.AuthResponse;
import com.souvanik.blog.auth.dto.LoginRequest;
import com.souvanik.blog.auth.dto.RefreshTokenRequest;
import com.souvanik.blog.auth.dto.RegisterRequest;
import com.souvanik.blog.auth.service.AuthService;
import com.souvanik.blog.common.SwaggerExamples;
import com.souvanik.blog.common.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * Copyright (c) 2025 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
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





    @Operation(summary = "Register a new user",
            description = """
              Creates a new user account with USER role.On success, returns JWT access and refresh tokens.
              """
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "User registered successfully",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "Success",
                                    value = SwaggerExamples.AUTH_SUCCESS
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Validation error",
                    content =  @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "ValidationError",
                                    value = SwaggerExamples.VALIDATION_ERROR
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "409",
                    description = "Email or username already exists",
                    content =@Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "ConflictError",
                                    value = SwaggerExamples.CONFLICT
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content =@Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "InternalServerError",
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









    @Operation(
            summary = "Login user",
            description = "Authenticates user and returns JWT access and refresh tokens."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Login successful",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Validation error"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Invalid credentials"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
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





    @Operation(
            summary = "Refresh access token",
            description = "Generates a new access token using a valid refresh token."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Token refreshed successfully",
                    content = @Content(schema = @Schema(implementation = AuthResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Invalid or expired refresh token"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Refresh token payload",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = RefreshTokenRequest.class),
                            examples = @ExampleObject(value = """
            {
              "refreshToken": "b7a3c2f1-2e8a-4d4e-9d77-3a2f1d8e9b10"
            }
            """)
                    )
            )
            @RequestBody @Valid RefreshTokenRequest request) {

        logger.debug("POST /auth/refresh");

        AuthResponse response = authService.refresh(request);
        return ResponseEntity.ok(ApiResponse.success(200, response));
    }


    @Operation(
            summary = "Logout user",
            description = "Revokes the refresh token so it can no longer be used."
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Logout successful"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid request"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Refresh token to revoke",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = RefreshTokenRequest.class),
                            examples = @ExampleObject(value = """
            {
              "refreshToken": "b7a3c2f1-2e8a-4d4e-9d77-3a2f1d8e9b10"
            }
            """)
                    )
            )
            @RequestBody @Valid RefreshTokenRequest request) {

        logger.debug("POST /auth/logout");

        authService.logout(request.getRefreshToken());
        return ResponseEntity.ok(ApiResponse.success(200, null));
    }
}