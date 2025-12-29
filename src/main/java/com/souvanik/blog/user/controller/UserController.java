package com.souvanik.blog.user.controller;

import com.souvanik.blog.common.api.ApiResponse;
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

import java.util.UUID;

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
            description = """
        Returns profile details of the currently authenticated user.
        User identity is derived from the JWT access token.
        """
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "User profile fetched successfully",
                    content = @io.swagger.v3.oas.annotations.media.Content(
                            schema = @io.swagger.v3.oas.annotations.media.Schema(
                                    implementation = com.souvanik.blog.user.dto.UserResponse.class
                            ),
                            examples = @io.swagger.v3.oas.annotations.media.ExampleObject(
                                    value = """
                {
                  "timestamp": "2025-01-01T10:15:30Z",
                  "status": 200,
                  "success": true,
                  "data": {
                    "id": "3fa85f64-5717-4562-b3fc-2c963f66afa6",
                    "username": "souvanik",
                    "email": "souvanik@example.com",
                    "bio": "Senior Java backend developer",
                    "role": "USER",
                    "status": "ACTIVE",
                    "createdAt": "2024-12-01T08:30:00Z"
                  },
                  "error": null,
                  "meta": null
                }
                """
                            )
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "User not found"
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
            description = """
        Updates profile details of the currently authenticated user.
        Only the logged-in user can update their own profile.
        """
    )
    @io.swagger.v3.oas.annotations.responses.ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Profile updated successfully",
                    content = @Content(schema = @Schema(implementation = UserResponse.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Validation error"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "User not found"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            )
    })
    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> updateMe(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Profile update payload",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = UpdateProfileRequest.class),
                            examples = @ExampleObject(value = """
            {
              "bio": "Senior Java backend developer"
            }
            """)
                    )
            )
            @RequestBody @Valid UpdateProfileRequest request) {

        logger.debug("PUT /api/v1/users/me - update profile");

        UserResponse updated = userService.updateCurrentUser(request);
        return ResponseEntity.ok(ApiResponse.success(200, updated));
    }
}
