# Blog Platform – Backend API

A **production ready backend system** for a modern blogging platform, built with **Spring Boot**, **JWT Authentication**, **role-based authorization**, and **clean REST API design**.

This project follows **enterprise best practices** including layered architecture, stateless security, global error handling, Swagger documentation, and audit-friendly logging.

---

##  Features Overview

###  Authentication & Security
- JWT-based authentication (Access + Refresh tokens)
- Secure login, registration, refresh, logout, logout-all
- Role-based authorization (`USER`, `ADMIN`)
- Stateless security using Spring Security
- Custom AuthenticationEntryPoint & AccessDeniedHandler
- Refresh token persistence and revocation

###  User Module
- Fetch current user profile
- Update own profile
- Secure access via JWT
- Method-level authorization

###  Post Module
- Create, read, update, delete posts
- Public access to published posts
- Draft & published post lifecycle
- Unique SEO-friendly slug generation
- Pagination support
- Tag management
- Ownership enforcement

###  Comment Module
- Add comments to posts
- Fetch comments for posts
- Visibility control
- Admin moderation support

###  Like System
- Like / Unlike posts
- Prevent duplicate likes
- Efficient count handling

###  Admin Module
- List all users (paginated)
- Block / unblock users
- Change post status
- Delete any post or comment
- Platform statistics dashboard
- Strict ADMIN-only access

###  API Documentation
- Fully documented using OpenAPI / Swagger
- JWT security scheme support
- Clear separation of public vs secured APIs
- Request/response examples for all endpoints

---

##  Tech Stack

| Layer | Technology |
|------|-----------|
| Language | Java 17 |
| Framework | Spring Boot |
| Security | Spring Security + JWT |
| Persistence | Spring Data JPA |
| Database | PostgreSQL |
| Migration | Flyway |
| Validation | Jakarta Validation |
| Documentation | Swagger / OpenAPI |
| Build Tool | Maven |
| Logging | SLF4J + Logback |

---

##  Design Principles
- Stateless APIs
- Clear separation of concerns
- Method-level authorization
- Centralized exception handling
- DTO-based API contracts
- Immutable response models

---

##  Authentication Flow

1. **Register / Login**
    - Returns access token + refresh token

2. **Access Token**
    - Short-lived
    - Sent via `Authorization: Bearer <token>`

3. **Refresh Token**
    - Stored securely in DB
    - Used to generate new access tokens
    - Can be revoked

4. **Logout**
    - Revokes one refresh token

5. **Logout All**
    - Revokes all refresh tokens for the user

>  Revoking refresh tokens does **NOT** invalidate existing access tokens until expiry  
> (this is correct JWT behavior)

---

## Roles & Permissions

| Role | Permissions |
|-----|------------|
| USER | Create posts, comments, likes, manage own profile |
| ADMIN | Full moderation: users, posts, comments, stats |

---

##  API Modules

###  Auth APIs (`/api/v1/auth`)
- `POST /register`
- `POST /login`
- `POST /refresh`
- `POST /logout`
- `POST /logout-all`

###  User APIs (`/api/v1/users`)
- `GET /me`
- `PUT /me`

###  Post APIs (`/api/v1/posts`)
- `POST /`
- `GET /`
- `GET /{slug}`
- `PUT /{id}`
- `DELETE /{id}`
- `POST /{id}/comments`
- `GET /{id}/comments`
- `POST /{id}/like`
- `DELETE /{id}/like`

###  Admin APIs (`/api/v1/admin`)
- `GET /users`
- `POST /users/{id}/block`
- `POST /users/{id}/unblock`
- `POST /posts/{id}/status`
- `DELETE /posts/{id}`
- `DELETE /comments/{id}`
- `GET /stats`

---

##  API Response Standard

All APIs follow a consistent response format:

```json
{
  "timestamp": "2025-01-01T10:15:30Z",
  "status": 200,
  "success": true,
  "data": {},
  "error": null,
  "meta": null
}
```

```json
{
  "timestamp": "2025-01-01T10:15:30Z",
  "status": 401,
  "success": false,
  "data": null,
  "error": {
    "code": "UNAUTHORIZED",
    "message": "Invalid or expired token"
  },
  "meta": null
}
```

---

##  Swagger

Swagger UI:
```
http://localhost:8080/swagger-ui.html
```

### JWT Authorization in Swagger
1. Click **Authorize**
2. Enter:
   ```
   Bearer <access-token>
   ```
3. Secured APIs unlock automatically
4. Public APIs work without a token

---

##  Configuration

### Required Environment Variables

```
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/blog
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=postgres

JWT_SECRET=your-secret-key
JWT_ACCESS_TOKEN_EXPIRY=15m
JWT_REFRESH_TOKEN_EXPIRY=7d
```

---

##  Testing Notes
- Public APIs - No token required
- Protected APIs - Access token required
- Refresh API - Uses refresh token only
- Logout-all - Requires valid access token

---

##  Run the Project with Docker (Step-by-Step)

This project uses **Docker**, **Docker Compose**, and **Flyway** to run the **Spring Boot backend** and **PostgreSQL 16** locally.

Database tables are **automatically created and versioned by Flyway migrations** on application startup.

---

###  Prerequisites

Make sure you have:

- Docker
- Docker Compose 
- Git

Check versions:

```bash
 docker --version
 docker compose version
```

---

###  Step 1: Clone the Repository

```bash
 git clone https://github.com/souvascloud/blog-platform-rest-api
 cd blog-platform-rest-api
```

---

### Step 2: Build the Application JAR

```bash
 mvn clean package
```

This creates the executable JAR in the `target/` directory.

---

### Step 3: Build Image & Start Containers

```bash
 docker compose up --build
```

This will:

- Build the Spring Boot Docker image
- Start PostgreSQL 16 in a container
- Wait for PostgreSQL to be ready
- Start the Spring Boot application
- Run Flyway migrations automatically
- Create required database tables

No manual SQL setup is needed.

---

###  Step 4: Access the Application

- **API Base URL**  
   http://localhost:8080

- **Swagger UI**  
   http://localhost:8080/swagger-ui.html

---

###  Step 5: Verify Database Migration (Optional)

Exec into PostgreSQL container:

```bash
 docker exec -it blog-postgres psql -U blog_user -d blog_db
```

Inside PostgreSQL:

```sql
\dt
```

You should see:
- Application tables
- `flyway_schema_history` table

Exit:

```sql
\q
```

---

###  Step 6: Exec into Containers (Optional)

Application container:

```bash
 docker exec -it blog-backend sh
```

PostgreSQL container:

```bash
 docker exec -it blog-postgres psql -U blog_user -d blog_db
```

---

###  Step 7: Stop the Application

```bash
 docker compose down
```

---

###  Optional: Reset Database

```bash
 docker compose down -v
```

This will delete all tables and data.  
Flyway will recreate the schema on the next startup.

---

## Notes

- Database schema is managed by Flyway
- Tables are created automatically on startup
- PostgreSQL runs fully inside Docker
- No local database setup is required
- Configuration is environment-driven
- PostgreSQL always starts before the application

---

## Summary

```bash
    git clone ...
    cd blog-platform
    mvn clean package
    docker compose up --build
```

**Flyway handles the database.  
Docker handles the infrastructure.**

---

##  Author

**Souvanik Saha**  
Senior Software Engineer

Passionate about backend systems, design, and clean code.

LinkedIn:  
https://www.linkedin.com/in/souvanik-saha
