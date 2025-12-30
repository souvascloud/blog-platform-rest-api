#  Architecture Documentation – Blog Platform Backend

This document describes the **overall system architecture**, **security design**, and **internal structure** of the Blog Platform backend application.

The architecture follows **enterprise-grade best practices** and is designed to be **secure, scalable, and maintainable**.

---

## 1. Architecture Overview

The Blog Platform backend is a **stateless RESTful system** built using **Spring Boot** and secured with **JWT-based authentication**.

### Key Characteristics
- Stateless APIs (no server-side sessions)
- JWT-based authentication & authorization
- Role-based access control (USER / ADMIN)
- Layered architecture
- Centralized error handling
- Cloud-ready and horizontally scalable

---

## 2. High-Level System Architecture

 **Diagram:** `system-architecture.png`

### Description
Clients interact with the backend through HTTP REST APIs.  
All requests pass through the security layer before reaching controllers.

### Components
- Clients (Web, Mobile, Swagger, Postman)
- API Gateway / Load Balancer
- Spring Boot Application
- PostgreSQL Database

### Flow
1. Client sends HTTP request
2. Request is validated by security filters
3. Authorized request reaches controllers
4. Business logic executed in service layer
5. Data persisted via repositories

---

## 3. Application Layered Architecture

 **Diagram:** `layered-architecture.png`

### Layers Explained

#### Controller Layer
- Handles HTTP requests & responses
- Input validation
- Swagger documentation
- No business logic

#### Service Layer
- Core business logic
- Authorization rules
- Transaction boundaries
- Method-level security (`@PreAuthorize`)

#### Security Layer
- JWT authentication filter
- CustomUserPrincipal
- SecurityContext management
- Custom 401 / 403 handlers

#### Persistence Layer
- Spring Data JPA repositories
- Database abstraction

---

## 4. Authentication & Authorization Architecture

 **Diagram:** `jwt-auth-flow.png`

### Authentication Flow
1. User logs in with credentials
2. Server issues:
   - Short-lived **Access Token**
   - Long-lived **Refresh Token**
3. Refresh token stored securely in DB

### Authorization Flow
1. Client sends access token in `Authorization` header
2. JWT filter validates token
3. User identity loaded into SecurityContext
4. Access controlled via roles & annotations

### Token Strategy
- Access tokens are **stateless**
- Refresh tokens are **revocable**
- Logout invalidates refresh tokens only

---

## 5. Spring Security Filter Chain

 **Diagram:** `security-filter-chain.png`

### Filter Order
1. CORS Filter
2. JWT Authentication Filter
3. SecurityContextHolderFilter
4. AuthorizationFilter
5. Controller

### Error Handling
- Invalid token → 401 Unauthorized
- Missing permission → 403 Forbidden
- Custom JSON error responses

---

## 6. Domain Model & Database Design

 **Diagram:** `domain-model.png`

### Core Entities
- User
- Post
- Comment
- PostLike
- RefreshToken

### Relationships
- User → Posts (1:N)
- Post → Comments (1:N)
- User ↔ Post (Likes)
- User → RefreshTokens (1:N)

---

## 7. Admin Module Architecture

 **Diagram:** `admin-authorization.png`

### Capabilities
- User moderation
- Content moderation
- Platform statistics
- Full system visibility

### Security
- Restricted to `ROLE_ADMIN`
- Enforced via method-level security

---

## 8. Error Handling Architecture

 **Diagram:** `exception-handling.png`

### Strategy
- Centralized exception handling
- Consistent error response format
- No stack traces leaked to clients

### Standard Error Response
```json
{
  "status": 401,
  "success": false,
  "error": {
    "code": "UNAUTHORIZED",
    "message": "Invalid or expired token"
  }
}
```

---

## 9. Scalability & Future Enhancements

- Horizontal scaling supported
- Stateless authentication
- Easy migration to microservices
- Redis cache support (future)
- Rate limiting & API gateway integration
- Event-driven extensions (Kafka)

---

## 10. Architecture Principles Followed

- Clean Architecture
- SOLID principles
- Security by design
- Separation of concerns
- Production ready defaults

---

## Author

**Souvanik Saha**  
Senior Software Engineer  
Java | Spring Boot | Backend Systems
