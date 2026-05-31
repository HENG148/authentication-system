# Authentication System — Spring Boot JWT REST API

A production-ready backend authentication system built with **Java Spring Boot**, **Spring Security**, **JWT**, and **PostgreSQL**. No frontend — pure REST API.

## 🛠️ Tech Stack

| Technology | Purpose |
|------------|---------|
| Java 21 | Programming language |
| Spring Boot 3.3.5 | Backend framework |
| Spring Security | Authentication & Authorization |
| JWT (JJWT 0.11.5) | Token-based authentication |
| Spring Data JPA | Database ORM |
| Hibernate | SQL query generation |
| PostgreSQL | Database |
| Maven | Dependency management |

## 📁 Project Structure

```
src/main/java/com/authentication/authentication_system/
├── AuthenticationSystemApplication.java   ← Entry point
├── model/
│   ├── User.java                          ← User entity (JPA)
│   └── Role.java                          ← Enum: USER, ADMIN
├── dto/
│   ├── RegisterRequest.java               ← Register input
│   ├── LoginRequest.java                  ← Login input
│   ├── AuthResponse.java                  ← JWT token response
│   ├── UserProfileResponse.java           ← Profile response
│   └── ErrorResponse.java                 ← Error response
├── repository/
│   └── UserRepository.java                ← Database queries
├── security/
│   ├── JwtService.java                    ← JWT generation & validation
│   └── JwtAuthenticationFilter.java       ← Request filter
├── config/
│   ├── ApplicationConfig.java             ← Security beans
│   └── SecurityConfig.java                ← HTTP security rules
├── service/
│   ├── AuthService.java                   ← Register & login logic
│   └── UserService.java                   ← User profile logic
├── controller/
│   ├── AuthController.java                ← Public auth endpoints
│   ├── UserController.java                ← User endpoints
│   └── AdminController.java               ← Admin endpoints
└── exception/
    ├── GlobalExceptionHandler.java        ← Centralized error handling
    ├── ResourceNotFoundException.java     ← 404
    ├── DuplicateResourceException.java    ← 409
    └── InvalidCredentialsException.java   ← 401
```

---

## ⚙️ Setup & Installation

### Prerequisites
- Java 21+
- Maven 3.6+
- PostgreSQL 14+

### 1. Clone the repository
```bash
git clone https://github.com/yourusername/authentication-system.git
cd authentication-system
```

### 2. Create the database
```sql
CREATE DATABASE auth_db;
```

### 3. Configure application.properties
Open `src/main/resources/application.properties` and update:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/auth_db
spring.datasource.username=postgres
spring.datasource.password=your_password
```

### 4. Run the application
```bash
mvn spring-boot:run
```

The server starts at `http://localhost:8080`

---

## 📡 API Endpoints

### Base URL: `http://localhost:8080`

| Method | Endpoint | Auth | Role | Description |
|--------|----------|:----:|:----:|-------------|
| POST | `/api/auth/register` | ❌ | - | Register new account |
| POST | `/api/auth/login` | ❌ | - | Login, get JWT token |
| POST | `/api/auth/logout` | ✅ | Any | Logout |
| GET | `/api/user/profile` | ✅ | Any | Get own profile |
| GET | `/api/user/me` | ✅ | Any | Get current username |
| GET | `/api/admin/dashboard` | ✅ | ADMIN | Dashboard stats |
| GET | `/api/admin/users` | ✅ | ADMIN | List all users |
| GET | `/api/admin/users/{id}` | ✅ | ADMIN | Get user by ID |

---

## 🧪 API Usage Examples

### Register
```http
POST /api/auth/register
Content-Type: application/json

{
  "username": "johndoe",
  "email": "john@example.com",
  "password": "secret123"
}
```
**Response 201:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "id": 1,
  "username": "johndoe",
  "email": "john@example.com",
  "role": "USER",
  "message": "Registration successful! Welcome, johndoe!"
}
```

### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "johndoe",
  "password": "secret123"
}
```
**Response 200:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "id": 1,
  "username": "johndoe",
  "email": "john@example.com",
  "role": "USER",
  "message": "Login successful! Welcome back, johndoe!"
}
```

### Get Profile (with token)
```http
GET /api/user/profile
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```
**Response 200:**
```json
{
  "id": 1,
  "username": "johndoe",
  "email": "john@example.com",
  "role": "USER",
  "createdAt": "2026-06-01T05:20:41"
}
```

### Admin Dashboard
```http
GET /api/admin/dashboard
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```
**Response 200:**
```json
{
  "message": "Welcome to the Admin Dashboard",
  "totalUsers": 2,
  "status": "System operational"
}
```

---

## 🔒 How Authentication Works

```
1. User registers or logs in
        ↓
2. Server verifies credentials & generates JWT token
        ↓
3. Client stores the token
        ↓
4. Client sends token in every request header:
   Authorization: Bearer <token>
        ↓
5. Server validates token on every request
        ↓
6. Access granted or denied based on role
```

---

## 👤 Roles

| Role | Access |
|------|--------|
| `USER` | `/api/user/**` endpoints |
| `ADMIN` | `/api/user/**` + `/api/admin/**` endpoints |

### Promote a user to ADMIN
Connect to your PostgreSQL database and run:
```sql
UPDATE users SET role = 'ADMIN' WHERE username = 'yourusername';
```
Then login again to get a new token with the ADMIN role.

---

## ❌ Error Responses

All errors follow this format:
```json
{
  "status": 401,
  "error": "Unauthorized",
  "message": "Invalid credentials. Please check your username and password.",
  "timestamp": "2026-06-01T05:20:41",
  "validationErrors": null
}
```

| Status | Meaning |
|--------|---------|
| 400 | Validation failed (bad input) |
| 401 | Unauthorized (wrong credentials or missing token) |
| 403 | Forbidden (wrong role) |
| 404 | Resource not found |
| 409 | Conflict (username or email already exists) |
| 500 | Internal server error |

---

## 🗄️ Database Schema

```sql
CREATE TABLE users (
    id         BIGSERIAL PRIMARY KEY,
    username   VARCHAR(50)  NOT NULL UNIQUE,
    email      VARCHAR(100) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    role       VARCHAR(255) NOT NULL,
    created_at TIMESTAMP    NOT NULL
);
```

> Tables are auto-created by Hibernate on startup (`ddl-auto=update`).

---

## 🔑 JWT Configuration

| Property | Value |
|----------|-------|
| Algorithm | HMAC SHA-256 |
| Expiration | 24 hours |
| Header | `Authorization: Bearer <token>` |

> ⚠️ Change the `jwt.secret` in `application.properties` before deploying to production.

---

## 📝 License

This project is open source and available under the [MIT License](LICENSE).
