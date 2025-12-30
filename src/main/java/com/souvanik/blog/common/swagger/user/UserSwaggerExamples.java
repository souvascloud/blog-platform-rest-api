package com.souvanik.blog.common.swagger.user;

/*
 * Copyright (c) 2025 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
public class UserSwaggerExamples {

    private UserSwaggerExamples() {}

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

}
