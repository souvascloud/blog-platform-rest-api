package com.souvanik.blog.common.swagger.post;

/*
 * Copyright (c) 2025 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
public class PostSwaggerExamples {

        private PostSwaggerExamples() {}

        public static final String POST_CREATE_SUCCESS =  """
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
        public static final String POST_DELETE_SUCCESS =  """
            {
              "timestamp": "2025-01-01T11:20:00Z",
              "status": 200,
              "success": true,
              "data": null,
              "error": null,
              "meta": null
            }
            """;
        public static final String POST_LIKE_SUCCESS =  """
            {
              "timestamp": "2025-01-01T11:35:00Z ",
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
        public static final String COMMENT_LIST_SUCCESS =  """
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
}
