# Global Exception Handler Guide

## Tổng quan
Global Exception Handler đã được cấu hình để xử lý tất cả các loại exception trong ứng dụng, cung cấp response format nhất quán và logging chi tiết.

## Các loại Exception được xử lý

### 🔐 Security Exceptions (401, 403)
- `AccessDeniedException` - 403 Forbidden
- `AuthenticationException` - 401 Unauthorized  
- `BadCredentialsException` - 401 Unauthorized

### 🗄️ JPA/Database Exceptions
- `DataIntegrityViolationException` - 409 Conflict (Duplicate entry, Foreign key constraint)
- `EmptyResultDataAccessException` - 404 Not Found
- `JpaObjectRetrievalFailureException` - 404 Not Found

### 🌐 HTTP Request Exceptions
- `HttpRequestMethodNotSupportedException` - 405 Method Not Allowed
- `NoHandlerFoundException` - 404 Not Found
- `MissingServletRequestParameterException` - 400 Bad Request
- `MethodArgumentTypeMismatchException` - 400 Bad Request

### 📝 JSON Parsing Exceptions
- `InvalidFormatException` - 400 Bad Request
- `JsonParseException` - 400 Bad Request

### ✅ Validation Exceptions
- `MethodArgumentNotValidException` - 400 Bad Request

### 🎯 Custom Exceptions
- `BaseException` - Custom status
- `ResourceNotFoundException` - 404 Not Found
- `DuplicateResourceException` - 409 Conflict
- `InvalidRequestException` - 400 Bad Request

## Response Format

Tất cả exceptions đều trả về format nhất quán sử dụng `Response<T>`:

```json
{
  "data": null,
  "message": null,
  "errorMessage": "Error description",
  "status": "Fail"
}
```

### Validation Errors:
```json
{
  "data": {
    "email": "Email is required",
    "password": "Password must be at least 6 characters"
  },
  "message": null,
  "errorMessage": "Validation failed",
  "status": "Fail"
}
```

## Cách sử dụng Custom Exceptions

### 1. ResourceNotFoundException
```java
// Trong service
if (user == null) {
    throw new ResourceNotFoundException("User", "id", userId);
}

// Hoặc
if (user == null) {
    throw new ResourceNotFoundException("User not found");
}
```

### 2. DuplicateResourceException
```java
// Trong service
if (userRepository.existsByEmail(email)) {
    throw new DuplicateResourceException("User", "email", email);
}
```

### 3. InvalidRequestException
```java
// Trong service
if (age < 0) {
    throw new InvalidRequestException("age", "must be positive");
}
```

## Ví dụ thực tế trong Command/Query Handlers

### Trong CreateUserCommandHandler:
```java
@CommandHandler
public Response<User> handle(CreateUserCommand command) {
    try {
        // Kiểm tra email đã tồn tại
        if (userRepository.findByEmail(command.getEmail()).isPresent()) {
            throw new DuplicateResourceException("User", "email", command.getEmail());
        }
        
        // Tạo user mới
        User user = User.builder()
            .email(command.getEmail())
            .password(passwordEncoder.encode(command.getPassword()))
            .firstName(command.getFirstName())
            .lastName(command.getLastName())
            .phone(command.getPhone())
            .avatar(command.getAvatar())
            .isEnabled(true)
            .build();
        
        User savedUser = userRepository.save(user);
        
        return Response.Success(savedUser, "Tạo tài khoản thành công");
            
    } catch (BaseException e) {
        // Re-throw custom exceptions
        throw e;
    } catch (Exception e) {
        log.error("Error creating user: {}", e.getMessage(), e);
        throw new InvalidRequestException("Failed to create user");
    }
}
```

### Trong GetUserByIdQueryHandler:
```java
@QueryHandler
public Response<User> handle(GetUserByIdQuery query) {
    try {
        User user = userRepository.findById(query.getId())
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", query.getId()));
        
        return Response.Success(user, "Lấy thông tin user thành công");
            
    } catch (BaseException e) {
        throw e;
    } catch (Exception e) {
        log.error("Error getting user by id: {}", e.getMessage(), e);
        throw new InvalidRequestException("Failed to get user");
    }
}
```

## HTTP Status Codes

| Exception | HTTP Status | Response Status |
|-----------|-------------|-----------------|
| ResourceNotFoundException | 404 | Fail |
| DuplicateResourceException | 409 | Fail |
| InvalidRequestException | 400 | Fail |
| AccessDeniedException | 403 | Fail |
| AuthenticationException | 401 | Fail |
| DataIntegrityViolationException | 409 | Fail |
| MethodArgumentNotValidException | 400 | Fail |
| DataAccessException | 500 | Fail |
| DeadlockLoserDataAccessException | 500 | Fail |
| TransactionSystemException | 500 | Fail |
| AsyncRequestTimeoutException | 408 | Fail |
| OutOfMemoryError | 500 | Fail |
| StackOverflowError | 500 | Fail |
| Exception (Generic) | 500 | Fail |

## Logging

Tất cả exceptions đều được log với:
- **Error level** cho tất cả exceptions
- **Stack trace** đầy đủ
- **Exception type** và message

```java
log.error("ExceptionType: {}", ex.getMessage(), ex);
```

## Best Practices

1. **Sử dụng Custom Exceptions** thay vì generic Exception
2. **Re-throw BaseException** trong catch blocks
3. **Log chi tiết** trước khi throw exception
4. **Sử dụng meaningful error codes** và messages
5. **Validate input** trước khi xử lý business logic

## Testing Exceptions

### Test với Postman:
```http
# Test 404 - Resource not found
GET /api/users/999999

# Test 409 - Duplicate email
POST /api/auth/register
{
  "email": "existing@email.com",
  "password": "password123"
}

# Test 400 - Invalid JSON
POST /api/auth/register
{
  "email": "invalid-json",
  "password": "password123"
}
```

### Expected Responses:
```json
// 404 Response
{
  "data": null,
  "message": null,
  "errorMessage": "User not found with id : '999999'",
  "status": "Fail"
}

// 409 Response
{
  "data": null,
  "message": null,
  "errorMessage": "User already exists with email : 'existing@email.com'",
  "status": "Fail"
}

// 400 Response
{
  "data": null,
  "message": null,
  "errorMessage": "Invalid JSON format: ...",
  "status": "Fail"
}

// 500 Response
{
  "data": null,
  "message": null,
  "errorMessage": "Internal server error. Please try again later.",
  "status": "Fail"
}
``` 