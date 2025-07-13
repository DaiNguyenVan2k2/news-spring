# Hướng dẫn sử dụng BaseController

## Tổng quan
BaseController giúp tránh việc inject CommandBus và QueryBus vào mọi controller, đồng thời cung cấp các method tiện ích để xử lý Commands và Queries.

## Các loại BaseController

### 1. BaseController (Cơ bản)
```java
public abstract class BaseController {
    @Autowired
    protected CommandBus commandBus;
    
    @Autowired
    protected QueryBus queryBus;
    
    protected <T> Response<T> executeCommand(Object command)
    protected <T> Response<T> executeQuery(Object query)
}
```

### 2. AdvancedBaseController (Nâng cao)
```java
public abstract class AdvancedBaseController extends BaseController {
    protected <T> ResponseEntity<Response<T>> executeCommandWithResponse(Object command)
    protected <T> ResponseEntity<Response<T>> executeQueryWithResponse(Object query)
    protected <T> ResponseEntity<Response<T>> executeCommandWithCustomStatus(Object command, HttpStatus successStatus)
    protected <T> ResponseEntity<Response<T>> executeQueryWithCustomStatus(Object query, HttpStatus successStatus)
}
```

## Cách sử dụng

### Cách 1: Sử dụng BaseController (Đơn giản)
```java
@RestController
@RequestMapping("/api/users")
public class UserController extends BaseController {

    @PostMapping
    public Response<User> createUser(@RequestBody CreateUserCommand command) {
        return executeCommand(command);
    }

    @GetMapping("/{id}")
    public Response<User> getUserById(@PathVariable Long id) {
        GetUserByIdQuery query = GetUserByIdQuery.builder()
            .id(id)
            .build();
        return executeQuery(query);
    }
}
```

### Cách 2: Sử dụng AdvancedBaseController (Với HTTP Status)
```java
@RestController
@RequestMapping("/api/admin")
public class AdminController extends AdvancedBaseController {

    @PostMapping("/users")
    public ResponseEntity<Response<User>> createUser(@RequestBody CreateUserCommand command) {
        // Trả về HTTP 201 Created khi thành công
        return executeCommandWithCustomStatus(command, HttpStatus.CREATED);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<Response<User>> getUserById(@PathVariable Long id) {
        GetUserByIdQuery query = GetUserByIdQuery.builder()
            .id(id)
            .build();
        // Trả về HTTP 200 OK khi thành công, 404 Not Found khi lỗi
        return executeQueryWithResponse(query);
    }
}
```

## Lợi ích của BaseController

### ✅ Trước khi có BaseController:
```java
@RestController
public class AuthController {
    @Autowired
    private CommandBus commandBus;
    
    @Autowired
    private QueryBus queryBus;
    
    @PostMapping("/register")
    public Response<User> register(@RequestBody CreateUserCommand command) {
        return commandBus.execute(command);
    }
}
```

### ✅ Sau khi có BaseController:
```java
@RestController
public class AuthController extends BaseController {
    
    @PostMapping("/register")
    public Response<User> register(@RequestBody CreateUserCommand command) {
        return executeCommand(command);
    }
}
```

## So sánh các method

| Method | Mục đích | Return Type | HTTP Status |
|--------|----------|-------------|-------------|
| `executeCommand()` | Thực thi command | `Response<T>` | Tự động xử lý |
| `executeQuery()` | Thực thi query | `Response<T>` | Tự động xử lý |
| `executeCommandWithResponse()` | Command với ResponseEntity | `ResponseEntity<Response<T>>` | 200 OK / 400 Bad Request |
| `executeQueryWithResponse()` | Query với ResponseEntity | `ResponseEntity<Response<T>>` | 200 OK / 404 Not Found |
| `executeCommandWithCustomStatus()` | Command với status tùy chỉnh | `ResponseEntity<Response<T>>` | Tùy chỉnh |
| `executeQueryWithCustomStatus()` | Query với status tùy chỉnh | `ResponseEntity<Response<T>>` | Tùy chỉnh |

## Ví dụ thực tế

### Controller đơn giản:
```java
@RestController
@RequestMapping("/api/products")
public class ProductController extends BaseController {

    @PostMapping
    public Response<Product> createProduct(@RequestBody CreateProductCommand command) {
        return executeCommand(command);
    }

    @GetMapping("/{id}")
    public Response<Product> getProduct(@PathVariable Long id) {
        GetProductQuery query = GetProductQuery.builder().id(id).build();
        return executeQuery(query);
    }

    @PutMapping("/{id}")
    public Response<Product> updateProduct(@PathVariable Long id, @RequestBody UpdateProductCommand command) {
        command.setId(id);
        return executeCommand(command);
    }
}
```

### Controller nâng cao:
```java
@RestController
@RequestMapping("/api/admin/products")
public class AdminProductController extends AdvancedBaseController {

    @PostMapping
    public ResponseEntity<Response<Product>> createProduct(@RequestBody CreateProductCommand command) {
        return executeCommandWithCustomStatus(command, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<Product>> getProduct(@PathVariable Long id) {
        GetProductQuery query = GetProductQuery.builder().id(id).build();
        return executeQueryWithResponse(query);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response<String>> deleteProduct(@PathVariable Long id) {
        DeleteProductCommand command = DeleteProductCommand.builder().id(id).build();
        return executeCommandWithCustomStatus(command, HttpStatus.NO_CONTENT);
    }
}
```

## Lưu ý quan trọng

1. **Kế thừa**: Controller phải extend BaseController hoặc AdvancedBaseController
2. **Exception Handling**: BaseController tự động xử lý exception và log lỗi
3. **Response Format**: Luôn trả về Response<T> hoặc ResponseEntity<Response<T>>
4. **HTTP Status**: AdvancedBaseController tự động set HTTP status phù hợp
5. **Logging**: Tự động log lỗi với SLF4J

## Khi nào sử dụng loại nào?

- **BaseController**: Khi chỉ cần xử lý Commands/Queries đơn giản
- **AdvancedBaseController**: Khi cần kiểm soát HTTP status codes hoặc ResponseEntity 