package com.souvanik.blog.common;

/*
 * Copyright (c) 2025 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
public final class SwaggerExamples {
    private SwaggerExamples() {}

    public static final String AUTH_SUCCESS = """
    {
      "timestamp": "2025-01-01T10:15:30Z",
      "status": 200,
      "success": true,
      "data": {
        "accessToken": "jwt-access-token",
        "refreshToken": "uuid-refresh-token",
        "tokenType": "Bearer",
        "expiresIn": 900
      },
      "error": null,
      "meta": null
    }
    """;

    public static final String VALIDATION_ERROR = """
    {
      "timestamp": "2025-01-01T10:15:30Z",
      "status": 400,
      "success": false,
      "data": null,
      "error": {
        "code": "VALIDATION_FAILED",
        "message": "Invalid request",
        "details": {
          "email": "must not be blank",
          "password": "must be at least 8 characters"
        }
      },
      "meta": null
    }
    """;

    public static final String UNAUTHORIZED = """
    {
      "timestamp": "2025-01-01T10:15:30Z",
      "status": 401,
      "success": false,
      "data": null,
      "error": {
        "code": "UNAUTHORIZED",
        "message": "Invalid credentials"
      },
      "meta": null
    }
    """;

    public static final String CONFLICT = """
    {
      "timestamp": "2025-01-01T10:15:30Z",
      "status": 409,
      "success": false,
      "data": null,
      "error": {
        "code": "RESOURCE_ALREADY_EXISTS",
        "message": "Email already exists"
      },
      "meta": null
    }
    """;

    public static final String INTERNAL_SERVER_ERROR = """
        {
          "timestamp": "2025-01-01T10:15:30Z",
          "status": 500,
          "success": false,
          "data": null,
          "error": {
            "code": "INTERNAL_SERVER_ERROR",
            "message": "Something went wrong. Please try again later."
          },
          "meta": null
        }
    """;
}
