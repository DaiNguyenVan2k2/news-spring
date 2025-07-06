package com.ptit.news.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ptit.news.command.dto.CreateUserCommand;
import com.ptit.news.command.dto.UpdateUserCommand;
import com.ptit.news.query.dto.GetUserByIdQuery;
import com.ptit.news.common.Response;
import com.ptit.news.entity.User;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AdminController extends AdvancedBaseController {

    @PostMapping("/users")
    public ResponseEntity<Response<User>> createUser(@RequestBody CreateUserCommand command) {
        // Return 201 Created for successful user creation
        return executeCommandWithCustomStatus(command, HttpStatus.CREATED);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<Response<User>> getUserById(@PathVariable Long id) {
        GetUserByIdQuery query = GetUserByIdQuery.builder()
                .id(id)
                .build();
        return executeQueryWithResponse(query);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<Response<User>> updateUser(@PathVariable Long id, @RequestBody UpdateUserCommand command) {
        command.setId(id);
        return executeCommandWithResponse(command);
    }
}