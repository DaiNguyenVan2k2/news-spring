package com.ptit.news.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/health")
@Tag(name = "Health Check", description = "Health check endpoints")
public class CheckHealthController {

    @GetMapping
    @Operation(summary = "Health check", description = "Returns Hello World to check if the service is running")
    public String checkHealth() {
        return "Hello World";
    }

    @GetMapping("/ping")
    @Operation(summary = "Ping", description = "Returns pong for quick health check")
    public String ping() {
        return "pong";
    }
}
