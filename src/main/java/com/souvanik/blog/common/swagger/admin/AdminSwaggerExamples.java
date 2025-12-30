package com.souvanik.blog.common.swagger.admin;

/*
 * Copyright (c) 2025 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
public class AdminSwaggerExamples {

    private AdminSwaggerExamples() {}

    public static final String ADMIN_USERS_LIST_SUCCESS =  """
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

    public static final String ADMIN_USER_BLOCK_SUCCESS ="""
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
