# Task Management System API Documentation

## Base URL
```
http://localhost:8080
```

## Authentication Endpoints

### 1. Register User
```http
POST /api/auth/register
```

**Request Body:**
```json
{
    "username": "johndoe",
    "email": "john.doe@example.com",
    "password": "password123"
}
```

**Success Response (200 OK):**
```json
{
    "id": 1,
    "username": "johndoe",
    "email": "john.doe@example.com",
    "roles": ["ROLE_USER"]
}
```

**Error Response (400 Bad Request):**
```json
{
    "username": "Username already exists",
    // or
    "email": "Email already exists"
}
```


### 2. Login
```http
POST /api/auth/login
```

**Request Body:**
```json
{
    "username": "johndoe",
    "password": "password123"
}
```

**Success Response (200 OK):**
```json
{
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "username": "johndoe",
    "email": "john.doe@example.com"
}
```

**Error Response (401 Unauthorized):**
```json
{
    "message": "Invalid username or password"
}
```

## Task Endpoints

### 1. Create Task
```http
POST /api/tasks
Authorization: Bearer {jwt_token}
```

**Request Body:**
```json
{
    "title": "Complete Project Documentation",
    "description": "Write comprehensive documentation for the REST API project",
    "status": "PENDING"
}
```

**Success Response (200 OK):**
```json
{
    "id": 1,
    "title": "Complete Project Documentation",
    "description": "Write comprehensive documentation for the REST API project",
    "status": "PENDING",
    "createdAt": "2024-03-19T14:30:00",
    "updatedAt": null
}
```

**Error Response (400 Bad Request):**
```json
{
    "title": "Title must be between 3 and 100 characters"
}
```

### 2. Get All Tasks (Paginated)
```http
GET /api/tasks?page=0&size=10&sort=createdAt,desc
Authorization: Bearer {jwt_token}
```

**Success Response (200 OK):**
```json
{
    "content": [
        {
            "id": 1,
            "title": "Complete Project Documentation",
            "description": "Write comprehensive documentation for the REST API project",
            "status": "PENDING",
            "createdAt": "2024-03-19T14:30:00",
            "updatedAt": null
        }
    ],
    "pageable": {
        "pageNumber": 0,
        "pageSize": 10,
        "sort": {
            "sorted": true,
            "unsorted": false,
            "empty": false
        }
    },
    "totalElements": 1,
    "totalPages": 1,
    "last": true,
    "first": true,
    "empty": false
}
```

### 3. Update Task
```http
PUT /api/tasks/{taskId}
Authorization: Bearer {jwt_token}
```

**Request Body:**
```json
{
    "title": "Updated Task Title",
    "description": "Updated task description",
    "status": "IN_PROGRESS"
}
```

**Success Response (200 OK):**
```json
{
    "id": 1,
    "title": "Updated Task Title",
    "description": "Updated task description",
    "status": "IN_PROGRESS",
    "createdAt": "2024-03-19T14:30:00",
    "updatedAt": "2024-03-19T15:45:00"
}
```

**Error Responses:**
```json
// 404 Not Found
{
    "message": "Task not found with id: 1"
}

// 403 Forbidden
{
    "message": "Not authorized to update this task"
}
```

### 4. Delete Task
```http
DELETE /api/tasks/{taskId}
Authorization: Bearer {jwt_token}
```

**Success Response (204 No Content)**

**Error Responses:**
```json
// 404 Not Found
{
    "message": "Task not found with id: 1"
}

// 403 Forbidden
{
    "message": "Not authorized to delete this task"
}
```

## Common Error Responses

### 401 Unauthorized
```json
{
    "message": "Full authentication is required to access this resource"
}
```

### 403 Forbidden
```json
{
    "message": "Access Denied"
}
```

## Task Status Values
The following status values are supported:
- `PENDING`
- `IN_PROGRESS`
- `COMPLETED`

## Pagination and Sorting

You can use the following query parameters for pagination and sorting:
- `page`: Page number (0-based)
- `size`: Number of items per page
- `sort`: Sort field and direction (e.g., `createdAt,desc` or `title,asc`)

Example:
```http
GET /api/tasks?page=0&size=10&sort=createdAt,desc
```

## Authentication

All endpoints except `/api/auth/register` and `/api/auth/login` require JWT authentication.
Include the JWT token in the Authorization header as follows:
```http
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

## Role-Based Access Control

- Users with `ROLE_USER` can only access and modify their own tasks
- Users with `ROLE_ADMIN` can access and modify all tasks
- By default, new users are assigned `ROLE_USER` 
