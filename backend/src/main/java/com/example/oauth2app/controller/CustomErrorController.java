package com.example.oauth2app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class CustomErrorController implements org.springframework.boot.web.servlet.error.ErrorController {
    
    @RequestMapping("/error")
    public ResponseEntity<Map<String, Object>> handleError(HttpServletRequest request) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        
        Map<String, Object> errorResponse = new HashMap<>();
        
        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
            errorResponse.put("status", statusCode);
            errorResponse.put("error", HttpStatus.valueOf(statusCode).getReasonPhrase());
        } else {
            errorResponse.put("status", 500);
            errorResponse.put("error", "Internal Server Error");
        }
        
        errorResponse.put("message", "An error occurred during OAuth2 authentication");
        errorResponse.put("path", request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI));
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}