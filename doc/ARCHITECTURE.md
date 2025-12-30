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
 
<img width="694" height="937" alt="system-arch" src="https://github.com/user-attachments/assets/3ac7b221-2edc-46ec-aa5a-0c8ce2341f84" />


### Description
Clients interact with the backend through HTTP REST APIs.  
All requests pass through the security layer before reaching controllers.

### Components
- Clients (Web, Mobile, Swagger, Postman)
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

<img width="1101" height="773" alt="layered-architecture" src="https://github.com/user-attachments/assets/772f5100-b158-423f-b39d-639a65f11319" />


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

 <img width="825" height="448" alt="jwt-flow" src="https://github.com/user-attachments/assets/168806f3-4802-4546-9a72-82528872e6f6" />

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

<img width="876" height="421" alt="security-filter-chain-sequnce" src="https://github.com/user-attachments/assets/e1bd0247-058f-4e66-b641-f6ab24fb3de3" />

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

<img width="518" height="685" alt="erd" src="https://github.com/user-attachments/assets/58af0446-18da-4a9f-b3a6-7d49ccf820b5" />


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

<img width="1258" height="593" alt="admin-flow" src="https://github.com/user-attachments/assets/744458d6-cd7d-4466-9ca5-35dd093829b1" />

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

<img width="718" height="501" alt="exception-flow" src="https://github.com/user-attachments/assets/8cb53644-8cd4-42d5-9457-8640e6740d7a" />

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
