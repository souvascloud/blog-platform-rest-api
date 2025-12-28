package com.souvanik.blog.common.exception;

import com.souvanik.blog.common.api.ApiError;
import com.souvanik.blog.common.api.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/*
 * Copyright (c) 2025 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<Void>> handleApiException(ApiException ex) {
        ApiError error = ApiError.builder()
                .code(ex.getErrorCode().name())
                .message(ex.getMessage())
                .build();

        return ResponseEntity
                .status(ex.getStatus())
                .body(ApiResponse.error(ex.getStatus().value(), error));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidation(MethodArgumentNotValidException ex) {
        List<String> details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .toList();

        ApiError error = ApiError.builder()
                .code(ErrorCode.VALIDATION_ERROR.name())
                .message("Validation failed")
                .details(details)
                .build();

        return ResponseEntity
                .badRequest()
                .body(ApiResponse.error(400, error));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolation(ConstraintViolationException ex) {
        List<String> details = ex.getConstraintViolations()
                .stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .toList();

        ApiError error = ApiError.builder()
                .code(ErrorCode.VALIDATION_ERROR.name())
                .message("Constraint violation")
                .details(details)
                .build();

        return ResponseEntity
                .badRequest()
                .body(ApiResponse.error(400, error));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleOther(Exception ex) {
        ApiError error = ApiError.builder()
                .code(ErrorCode.INTERNAL_ERROR.name())
                .message("Unexpected error occurred")
                .build();

        return ResponseEntity
                .status(500)
                .body(ApiResponse.error(500, error));
    }
}
