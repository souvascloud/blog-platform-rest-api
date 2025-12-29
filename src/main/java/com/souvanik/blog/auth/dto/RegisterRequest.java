package com.souvanik.blog.auth.dto;

import com.souvanik.blog.common.validation.StrongPassword;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

/*
 * Copyright (c) 2025 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
@Schema(description = "Request payload to register a new user")
@Getter
public class RegisterRequest {

    @Schema(description = "Unique username", example = "souvanik")
    @NotBlank
    @Size(min = 3, max = 50)
    private String username;


    @Schema(description = "User email address", example = "souvanik@example.com")
    @NotBlank
    @Email
    private String email;


    @Schema(description = "Strong account password", example = "StrongPass@123")
    @NotBlank
    @StrongPassword
    private String password;
}
