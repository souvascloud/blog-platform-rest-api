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

    public static final String LOGOUT_UNAUTHORIZED = """
    {
      "timestamp": "2025-01-01T10:30:00Z",
      "status": 401,
      "success": false,
      "data": null,
      "error": {
        "code": "TOKEN_INVALID",
        "message": "Invalid or expired refresh token"
      },
      "meta": null
    }
    """;

    public static final String LOGOUT_FORBIDDEN = """
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

    public static final String REFRESH_TOKEN_REQUEST = """
{
  "refreshToken": "Lh0Z3u0ZKpN9ZPpYxFqHcWq9Y8uXk2Rr4mZ0d5QvEoA"
}
""";

    public static final String LOGOUT_ALL_SUCCESS = """
{
  "timestamp": "2025-01-01T11:00:00Z",
  "status": 200,
  "success": true,
  "data": null,
  "error": null,
  "meta": null
}
""";

    public static final String USER_PROFILE_SUCCESS = """
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
""";


    public static final String USER_PROFILE_UPDATE_SUCCESS = """
        {
          "timestamp": "2025-01-01T10:20:30Z",
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
        """;

    // ------------------------------------------------------
    // RESOURCE NOT FOUND
    // ------------------------------------------------------
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

    public static final String POST_CREATE_SUCCESS = """
{
  "timestamp": "2025-01-01T11:00:00Z",
  "status": 200,
  "success": true,
  "data": {
    "id": "b1b2c3d4-1234-5678-9999-acde12345678",
    "title": "Spring Boot REST API Best Practices",
    "slug": "spring-boot-rest-api-best-practices",
    "content": "In this post we explore REST API design...",
    "status": "PUBLISHED",
    "author": "souvanik",
    "tags": ["spring", "java", "backend"],
    "likes": 0,
    "createdAt": "2025-01-01T10:59:30Z"
  },
  "error": null,
  "meta": null
}
""";

    public static final String POST_FETCH_SUCCESS = """
{
  "timestamp": "2025-01-01T11:05:00Z",
  "status": 200,
  "success": true,
  "data": {
    "id": "b1b2c3d4-1234-5678-9999-acde12345678",
    "title": "Spring Boot REST API Best Practices",
    "slug": "spring-boot-rest-api-best-practices",
    "content": "In this post we explore REST API design...",
    "status": "PUBLISHED",
    "author": "souvanik",
    "tags": ["spring", "java", "backend"],
    "likes": 12,
    "createdAt": "2025-01-01T10:59:30Z"
  },
  "error": null,
  "meta": null
}
""";


    public static final String POST_LIST_SUCCESS = """
{
  "timestamp": "2025-01-01T11:10:00Z",
  "status": 200,
  "success": true,
  "data": [
    {
      "id": "b1b2c3d4-1234-5678-9999-acde12345678",
      "title": "Spring Boot REST API Best Practices",
      "slug": "spring-boot-rest-api-best-practices",
      "status": "PUBLISHED",
      "author": "souvanik",
      "likes": 12,
      "createdAt": "2025-01-01T10:59:30Z"
    },
    {
      "id": "c2d3e4f5-2234-5678-9999-acde98765432",
      "title": "JWT Authentication Explained",
      "slug": "jwt-authentication-explained",
      "status": "PUBLISHED",
      "author": "souvanik",
      "likes": 7,
      "createdAt": "2025-01-01T09:30:00Z"
    }
  ],
  "error": null,
  "meta": {
    "page": 0,
    "size": 10,
    "totalElements": 2,
    "totalPages": 1
  }
}
""";

    public static final String POST_UPDATE_SUCCESS = """
{
  "timestamp": "2025-01-01T11:15:00Z",
  "status": 200,
  "success": true,
  "data": {
    "id": "b1b2c3d4-1234-5678-9999-acde12345678",
    "title": "Spring Boot REST API Best Practices – Updated",
    "slug": "spring-boot-rest-api-best-practices",
    "content": "Updated content with new best practices...",
    "status": "PUBLISHED",
    "author": "souvanik",
    "tags": ["spring", "java", "api"],
    "likes": 12,
    "createdAt": "2025-01-01T10:59:30Z"
  },
  "error": null,
  "meta": null
}
""";

    public static final String POST_DELETE_SUCCESS = """
{
  "timestamp": "2025-01-01T11:20:00Z",
  "status": 200,
  "success": true,
  "data": null,
  "error": null,
  "meta": null
}
""";

    public static final String FORBIDDEN = """
{
  "timestamp": "2025-01-01T11:21:00Z",
  "status": 403,
  "success": false,
  "data": null,
  "error": {
    "code": "FORBIDDEN",
    "message": "You are not allowed to perform this action"
  },
  "meta": null
}
""";
    public static final String COMMENT_ADD_SUCCESS = """
{
  "timestamp": "2025-01-01T11:25:00Z",
  "status": 200,
  "success": true,
  "data": {
    "id": "c1234567-89ab-4def-9012-abcdef123456",
    "author": "souvanik",
    "content": "This post explained REST API design really well!",
    "createdAt": "2025-01-01T11:24:30Z"
  },
  "error": null,
  "meta": null
}
""";
    public static final String COMMENT_LIST_SUCCESS = """
{
  "timestamp": "2025-01-01T11:30:00Z",
  "status": 200,
  "success": true,
  "data": [
    {
      "id": "c1234567-89ab-4def-9012-abcdef123456",
      "author": "souvanik",
      "content": "Great post!",
      "createdAt": "2025-01-01T11:24:30Z"
    },
    {
      "id": "d2345678-90ab-4def-9012-fedcba654321",
      "author": "john_doe",
      "content": "Very helpful explanation.",
      "createdAt": "2025-01-01T11:26:10Z"
    }
  ],
  "error": null,
  "meta": null
}


""";
    public static final String POST_LIKE_SUCCESS = """
{
  "timestamp": "2025-01-01T11:35:00Z",
  "status": 200,
  "success": true,
  "data": null,
  "error": null,
  "meta": null
}
""";

    public static final String POST_UNLIKE_SUCCESS = """
{
  "timestamp": "2025-01-01T11:36:00Z",
  "status": 200,
  "success": true,
  "data": null,
  "error": null,
  "meta": null
}
""";

    public static final String ADMIN_USERS_LIST_SUCCESS = """
        {
          "timestamp": "2025-01-01T10:00:00Z",
          "status": 200,
          "success": true,
          "data": [
            {
              "id": "11111111-1111-1111-1111-111111111111",
              "username": "souvanik",
              "email": "souvanik@example.com",
              "role": "USER",
              "status": "ACTIVE",
              "createdAt": "2024-12-01T08:30:00Z"
            },
            {
              "id": "22222222-2222-2222-2222-222222222222",
              "username": "admin",
              "email": "admin@example.com",
              "role": "ADMIN",
              "status": "ACTIVE",
              "createdAt": "2024-11-15T09:10:00Z"
            }
          ],
          "error": null,
          "meta": {
            "page": 0,
            "size": 20,
            "totalElements": 2,
            "totalPages": 1
          }
        }
        """;

    public static final String ADMIN_USER_BLOCK_SUCCESS = """
        {
          "timestamp": "2025-01-01T10:05:00Z",
          "status": 200,
          "success": true,
          "data": null,
          "error": null,
          "meta": null
        }
        """;

    public static final String ADMIN_USER_UNBLOCK_SUCCESS = """
        {
          "timestamp": "2025-01-01T10:06:00Z",
          "status": 200,
          "success": true,
          "data": null,
          "error": null,
          "meta": null
        }
        """;

    // =========================================================
    // ADMIN – POSTS
    // =========================================================

    public static final String ADMIN_POST_STATUS_UPDATE_SUCCESS = """
        {
          "timestamp": "2025-01-01T10:10:00Z",
          "status": 200,
          "success": true,
          "data": null,
          "error": null,
          "meta": null
        }
        """;

    public static final String ADMIN_POST_DELETE_SUCCESS = """
        {
          "timestamp": "2025-01-01T10:12:00Z",
          "status": 200,
          "success": true,
          "data": null,
          "error": null,
          "meta": null
        }
        """;

    // =========================================================
    // ADMIN – COMMENTS
    // =========================================================

    public static final String ADMIN_COMMENT_DELETE_SUCCESS = """
        {
          "timestamp": "2025-01-01T10:15:00Z",
          "status": 200,
          "success": true,
          "data": null,
          "error": null,
          "meta": null
        }
        """;

    // =========================================================
    // ADMIN – STATS
    // =========================================================

    public static final String ADMIN_STATS_SUCCESS = """
        {
          "timestamp": "2025-01-01T10:20:00Z",
          "status": 200,
          "success": true,
          "data": {
            "totalUsers": 1200,
            "totalPosts": 4500,
            "totalComments": 18200,
            "totalLikes": 56000
          },
          "error": null,
          "meta": null
        }
        """;


}
