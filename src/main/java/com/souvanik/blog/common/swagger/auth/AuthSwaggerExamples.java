package com.souvanik.blog.common.swagger.auth;

/*
 * Copyright (c) 2025 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
public class AuthSwaggerExamples {

    private AuthSwaggerExamples() {}


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
    public static final String REFRESH_TOKEN_REQUEST = """
        {
          "refreshToken": "Lh0Z3u0ZKpN9ZPpYxFqHcWq9Y8uXk2Rr4mZ0d5QvEoA"
        }
        """;
    public static final String TOKEN_EXPIRED = """
        {
          "timestamp": "2025-01-01T10:15:30Z",
          "status": 401,
          "success": false,
          "data": null,
          "error": {
            "code": "TOKEN_EXPIRED",
            "message": "Access token has expired"
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
    public static final String LOGOUT_SUCCESS = """
        {
          "timestamp": "2025-01-01T10:30:00Z",
          "status": 200,
          "success": true,
          "data": null,
          "error": null,
          "meta": null
        }
        """;

}

