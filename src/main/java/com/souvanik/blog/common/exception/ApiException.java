package com.souvanik.blog.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/*
 * Copyright (c) 2025 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
@Getter
public abstract class ApiException extends RuntimeException{
    private final ErrorCode errorCode;
    private final HttpStatus status;

    protected ApiException(ErrorCode errorCode, String message, HttpStatus status) {
        super(message);
        this.errorCode = errorCode;
        this.status = status;
    }
}
