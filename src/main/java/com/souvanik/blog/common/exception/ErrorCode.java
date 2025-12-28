package com.souvanik.blog.common.exception;

/*
 * Copyright (c) 2025 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
public enum ErrorCode {
    // Generic
    INTERNAL_ERROR,
    INVALID_REQUEST,
    VALIDATION_ERROR,
    RESOURCE_NOT_FOUND,
    RESOURCE_CONFLICT,
    UNAUTHORIZED,
    FORBIDDEN,

    // Auth
    INVALID_CREDENTIALS,
    TOKEN_EXPIRED,
    TOKEN_INVALID,

    // User
    EMAIL_ALREADY_EXISTS,
    USERNAME_ALREADY_EXISTS,
    USER_BLOCKED,

    // Post
    POST_NOT_FOUND,
    SLUG_ALREADY_EXISTS,

    // Comment
    COMMENT_NOT_FOUND,

    // Like
    ALREADY_LIKED,
    NOT_LIKED
}
