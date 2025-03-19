# Task Management System

A Spring Boot REST API for managing tasks with JWT authentication and role-based authorization.

## Features

- User authentication with JWT
- Role-based authorization (ADMIN and USER roles)
- CRUD operations for tasks
- Pagination and sorting support
- MySQL database integration
- Docker support

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- MySQL 8.0 or higher
- Docker (optional)

## Getting Started

1. Clone the repository:
```bash
git clone https://github.com/Mr-AB007/task-management.git
cd task-management-system
```

2. Configure MySQL:
- Make sure MySQL is running on port 3306
- The application will automatically create a database named `task_management`
- Update `src/main/resources/application.properties` with your MySQL credentials if different from:
  ```properties
  spring.datasource.username=root
  spring.datasource.password=root
  ```

3. Build the project:
```bash
mvn clean install
```

4. Run the application:
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## API Endpoints

### Authentication

- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - Login and get JWT token

### Tasks

- `POST /api/tasks` - Create a new task
- `GET /api/tasks` - Get all tasks (paginated)
- `PUT /api/tasks/{id}` - Update a task
- `DELETE /api/tasks/{id}` - Delete a task

## Docker Support

1. Build the Docker image:
```bash
docker build -t task-management .
```

2. Run the container:
```bash
docker run -p 8080:8080 task-management
```

## Security

- JWT-based authentication
- BCrypt password hashing
- Role-based access control
- Stateless session management

## Testing

Run the tests using:
```bash
mvn test
```

## Sample API Requests

### Register a new user
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123"
  }'
```

### Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

### Create a task (with JWT token)
```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Test Task",
    "description": "This is a test task",
    "status": "PENDING"
  }'
``` 
