package com.souvanik.blog.user.controller;

import com.souvanik.blog.common.api.ApiResponse;
import com.souvanik.blog.common.config.OpenApiConfig;
import com.souvanik.blog.user.dto.UpdateProfileRequest;
import com.souvanik.blog.user.dto.UserResponse;
import com.souvanik.blog.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.souvanik.blog.common.swagger.CommonSwaggerExamples.*;
import static com.souvanik.blog.common.swagger.auth.AuthSwaggerExamples.UNAUTHORIZED;
import static com.souvanik.blog.common.swagger.user.UserSwaggerExamples.USER_PROFILE_SUCCESS;
import static com.souvanik.blog.common.swagger.user.UserSwaggerExamples.USER_PROFILE_UPDATE_SUCCESS;

/*
 * Copyright (c) 2025 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
@Tag(
        name = "User",
        description = "APIs for managing current user profile"
)
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @Operation(
            summary = "Get current user profile",
            description = "Returns profile details of the currently authenticated user. Requires JWT access token",
            security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(
                    name = OpenApiConfig.SECURITY_SCHEME_NAME
            )
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Profile fetched successfully",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = USER_PROFILE_SUCCESS
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = UNAUTHORIZED
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = RESOURCE_NOT_FOUND
                            )
                    )
            )
    })
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getMe() {
        logger.debug("GET /api/v1/users/me");

        UserResponse user = userService.getCurrentUser();
        return ResponseEntity.ok(ApiResponse.success(200, user));
    }





    @Operation(
            summary = "Update current user profile",
            description = "Partially updates profile details of the authenticated user.Requires JWT access toke",
            security = @io.swagger.v3.oas.annotations.security.SecurityRequirement(
                    name = OpenApiConfig.SECURITY_SCHEME_NAME
            )
    )
    @io.swagger.v3.oas.annotations.security.SecurityRequirement(name = "bearerAuth")
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Profile updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = USER_PROFILE_UPDATE_SUCCESS
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Validation error",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = VALIDATION_ERROR
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = UNAUTHORIZED
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = INTERNAL_SERVER_ERROR
                            )
                    )
            )
    })
    @PatchMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> updateMe(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Profile update payload",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = UpdateProfileRequest.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "bio": "Senior Java backend developer"
                                            }
                                            """
                            )
                    )
            )
            @RequestBody @Valid UpdateProfileRequest request) {

        logger.debug("PUT /api/v1/users/me - update profile");

        UserResponse updated = userService.updateCurrentUser(request);
        return ResponseEntity.ok(ApiResponse.success(200, updated));
    }
}
