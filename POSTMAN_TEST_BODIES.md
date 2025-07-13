# Postman Test Bodies cho CQRS API

## 1. Đăng ký tài khoản (Register)
**Method:** POST  
**URL:** `http://localhost:8080/api/auth/register`

```json
{
  "email": "test@example.com",
  "password": "password123",
  "firstName": "Nguyễn",
  "lastName": "Văn A",
  "phone": "0123456789",
  "avatar": "https://example.com/avatar.jpg"
}
```

## 2. Đăng nhập (Sign-in)
**Method:** POST  
**URL:** `http://localhost:8080/api/auth/sign-in`

```json
{
  "email": "test@example.com",
  "password": "password123"
}
```

## 3. Cập nhật thông tin user
**Method:** PUT  
**URL:** `http://localhost:8080/api/auth/user`

```json
{
  "id": 1,
  "firstName": "Nguyễn",
  "lastName": "Văn B",
  "phone": "0987654321",
  "avatar": "https://example.com/new-avatar.jpg"
}
```

## 4. Lấy thông tin user theo email
**Method:** GET  
**URL:** `http://localhost:8080/api/auth/user/test@example.com`

*Không cần body, chỉ cần thay email trong URL*

## 5. Admin - Tạo user (với ResponseEntity)
**Method:** POST  
**URL:** `http://localhost:8080/api/admin/users`

```json
{
  "email": "admin@example.com",
  "password": "admin123",
  "firstName": "Admin",
  "lastName": "User",
  "phone": "0123456789",
  "avatar": "https://example.com/admin-avatar.jpg"
}
```

## 6. Admin - Lấy user theo ID
**Method:** GET  
**URL:** `http://localhost:8080/api/admin/users/1`

*Không cần body*

## 7. Admin - Cập nhật user
**Method:** PUT  
**URL:** `http://localhost:8080/api/admin/users/1`

```json
{
  "firstName": "Updated",
  "lastName": "Name",
  "phone": "1111111111",
  "avatar": "https://example.com/updated-avatar.jpg"
}
```

## 8. User - Lấy user theo ID
**Method:** GET  
**URL:** `http://localhost:8080/api/users/1`

*Không cần body*

## 9. User - Cập nhật user
**Method:** PUT  
**URL:** `http://localhost:8080/api/users/1`

```json
{
  "firstName": "User",
  "lastName": "Updated",
  "phone": "2222222222",
  "avatar": "https://example.com/user-avatar.jpg"
}
```

---

## Headers cần thiết:
```
Content-Type: application/json
Accept: application/json
```

## Test Flow đề xuất:

1. **Đăng ký tài khoản** → Lưu lại user ID từ response
2. **Đăng nhập** → Lưu lại token từ response
3. **Lấy thông tin user** → Kiểm tra thông tin
4. **Cập nhật thông tin** → Kiểm tra thay đổi
5. **Test các endpoint admin** → So sánh response format

## Expected Responses:

### Success Response:
```json
{
  "data": {
    "id": 1,
    "email": "test@example.com",
    "firstName": "Nguyễn",
    "lastName": "Văn A",
    "phone": "0123456789",
    "avatar": "https://example.com/avatar.jpg",
    "isEnabled": true
  },
  "message": "Tạo tài khoản thành công",
  "status": "Success"
}
```

### Error Response:
```json
{
  "data": null,
  "message": null,
  "errorMessage": "Email đã tồn tại",
  "status": "Fail"
}
```

### Sign-in Success Response:
```json
{
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "user": {
      "id": 1,
      "email": "test@example.com",
      "firstName": "Nguyễn",
      "lastName": "Văn A"
    },
    "tokenType": "Bearer"
  },
  "message": "Đăng nhập thành công",
  "status": "Success"
}
``` 