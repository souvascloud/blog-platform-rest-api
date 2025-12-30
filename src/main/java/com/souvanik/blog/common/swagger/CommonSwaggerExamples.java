package com.souvanik.blog.common.swagger;

/*
 * Copyright (c) 2025 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
public class CommonSwaggerExamples {

    private CommonSwaggerExamples() {}

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
    public static final String RESOURCE_NOT_FOUND = """
        {
          "timestamp": "2025-01-01T10:22:15Z",
          "status": 404,
          "success": false,
          "data": null,
          "error": {
            "code": "RESOURCE_NOT_FOUND",
            "message": "User not found",
            "timestamp": "2025-01-01T10:22:15Z",
            "details": null
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
    public static final String FORBIDDEN = """
    {
      "timestamp": "2025-01-01T10:30:00Z",
      "status": 403,
      "success": false,
      "data": null,
      "error": {
        "code": "USER_BLOCKED",
        "message": "User account is blocked"
      },
      "meta": null
    }
    """;
    public static final String INTERNAL_SERVER_ERROR =  """
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
