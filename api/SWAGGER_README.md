# Swagger Documentation - Unilink API

## Overview

The Unilink API is documented using **Swagger/OpenAPI 3** via the `springdoc-openapi` library. This documentation provides an interactive interface to explore and test all API endpoints.

## How to Access

### 1. Swagger UI Interface

After starting the application, access Swagger UI at:

```
http://localhost:8080/swagger-ui.html
```

### 2. OpenAPI Specification (JSON)

The OpenAPI specification in JSON format is available at:

```
http://localhost:8080/api-docs
```

## Configuration

### `application.properties` Settings

```properties
# Swagger/OpenAPI Configuration
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.operationsSorter=method
springdoc.swagger-ui.tagsSorter=alpha
springdoc.swagger-ui.doc-expansion=none
```

### API Configuration

The main OpenAPI configuration is in the `OpenApiConfig.java` class and defines:

* API title and description
* Contact information
* License
* Available servers

## Documented Endpoints

### 🔐 Authentication

* **POST** `/api/auth/login` - User login

### 👥 Users

* **GET** `/api/users` - List all users
* **GET** `/api/users/{id}` - Get user by ID
* **POST** `/api/users` - Create new user
* **PUT** `/api/users/{id}` - Update user
* **DELETE** `/api/users/{id}` - Delete user

### 📋 Projects

* **GET** `/api/projects` - List projects (optional filters supported)
* **GET** `/api/projects/{id}` - Get project by ID
* **POST** `/api/projects` - Create new project
* **PUT** `/api/projects/{id}` - Update project
* **DELETE** `/api/projects/{id}` - Delete project

### 🏢 Centers

* **GET** `/api/centers` - List all centers
* **GET** `/api/centers/{id}` - Get center by ID
* **POST** `/api/centers` - Create new center
* **PUT** `/api/centers/{id}` - Update center
* **DELETE** `/api/centers/{id}` - Delete center

### 🏷️ Tags

* **GET** `/api/tags` - List all tags
* **GET** `/api/tags/{id}` - Get tag by ID
* **POST** `/api/tags` - Create new tag
* **PUT** `/api/tags/{id}` - Update tag
* **DELETE** `/api/tags/{id}` - Delete tag

## Documented DTOs

### UserRequestDTO

```json
{
  "name": "John Silva",
  "email": "john.silva@email.com",
  "password": "password123",
  "role": "STUDENT"
}
```

### ProjectRequestDTO

```json
{
  "name": "School Management System",
  "description": "Complete system for managing schools",
  "centerId": "center-uuid",
  "ownerId": "owner-uuid",
  "openForApplications": true,
  "imgUrl": "https://example.com/project-image.jpg",
  "teamSize": 5,
  "tagsToBeAdded": ["tag-uuid1", "tag-uuid2"],
  "tagsToBeRemoved": ["tag-uuid3"]
}
```

### CenterRequestDTO

```json
{
  "name": "Technology Center",
  "centerUrl": "https://technology-center.com"
}
```

### TagRequestDTO

```json
{
  "name": "Java",
  "colorHex": "#FF5733"
}
```

## Response Codes

### Success

* **200** - Operation successful
* **204** - Operation successful (no content)

### Error

* **400** - Invalid data provided
* **401** - Invalid credentials
* **404** - Resource not found

## Using the Swagger Interface

1. **Access** `http://localhost:8080/swagger-ui.html`
2. **Browse** endpoints organized by tags
3. **Click** an endpoint to expand it
4. **Click** "Try it out" to test
5. **Fill in** required parameters
6. **Execute** the request
7. **View** the response

## Benefits of Swagger Documentation

* ✅ **Interactive Interface** - Test endpoints directly in the browser
* ✅ **Automatic Documentation** - Always up to date with the code
* ✅ **Usage Examples** - DTOs with practical examples
* ✅ **Response Codes** - Full documentation of responses
* ✅ **Validation** - Interface validates data before submission
* ✅ **Export** - Generate OpenAPI spec for other tools

## Development

To document new endpoints:

1. **Import** Swagger annotations:

```java
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
```

2. **Add** `@Tag` to the class:

```java
@Tag(name = "Tag Name", description = "Description of the endpoints")
```

3. **Document** each method with `@Operation`:

```java
@Operation(
    summary = "Operation summary",
    description = "Detailed description"
)
@ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Success"),
    @ApiResponse(responseCode = "400", description = "Error")
})
```

4. **Document** parameters with `@Parameter` and `@Schema`:

```java
@Parameter(description = "Parameter description")
@Schema(description = "DTO description")
```
