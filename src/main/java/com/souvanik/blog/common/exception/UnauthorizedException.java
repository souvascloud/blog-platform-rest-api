package com.souvanik.blog.common.exception;

import org.springframework.http.HttpStatus;

/*
 * Copyright (c) 2025 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
public class UnauthorizedException extends ApiException {

    public UnauthorizedException(ErrorCode code, String message) {
        super(code, message, HttpStatus.UNAUTHORIZED);
    }
}