package com.example.student_management.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HealthController {

    @GetMapping({"/", "/health", "/api/health"})
    public ResponseEntity<Map<String, Object>> checkHealth() {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "UP");
        status.put("service", "Smart AI Student Management Backend");
        status.put("version", "1.0.0");
        status.put("timestamp", Instant.now().toString());
        status.put("statusMessage", "All services operating normally");
        return ResponseEntity.ok(status);
    }
}
