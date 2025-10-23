package com.example.oauth2app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class DatabaseTestController {
    
    @Autowired
    private DataSource dataSource;
    
    @GetMapping("/test-db")
    public ResponseEntity<Map<String, Object>> testDatabase() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Connection connection = dataSource.getConnection();
            response.put("status", "success");
            response.put("message", "Database connection successful");
            response.put("url", connection.getMetaData().getURL());
            connection.close();
        } catch (SQLException e) {
            response.put("status", "error");
            response.put("message", "Database connection failed: " + e.getMessage());
            response.put("error", e.getClass().getSimpleName());
        }
        
        return ResponseEntity.ok(response);
    }
}
