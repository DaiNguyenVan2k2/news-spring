
package com.ptit.news.controller;

import org.springframework.web.bind.annotation.RestController;

import com.ptit.news.common.Response;
import com.ptit.news.common.enums.StatusResponse;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AuthController {
    @PostMapping("api/auth/login")
    public Response<String> login() {
        Response<String> response = new Response<>();

        // Call database => insert // insert không được
        response.data = "Tạo tài khoản thành công";
        response.errorMessage = null;
        response.message = "Success";
        response.status = StatusResponse.Fail;

        // Define Response

        return response;
    }
}
