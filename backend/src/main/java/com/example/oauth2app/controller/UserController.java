package com.example.oauth2app.controller;

import com.example.oauth2app.entity.User;
import com.example.oauth2app.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/user")
    public ResponseEntity<Map<String, Object>> getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Map<String, Object> response = new HashMap<>();
        
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            response.put("authenticated", true);
            response.put("user", Map.of(
                "id", user.getId(),
                "email", user.getEmail(),
                "displayName", user.getDisplayName(),
                "avatarUrl", user.getAvatarUrl() != null ? user.getAvatarUrl() : "",
                "bio", user.getBio() != null ? user.getBio() : "",
                // --- FIX 1 ---
                "createdAt", user.getCreatedAt() != null ? user.getCreatedAt() : "",
                "updatedAt", user.getUpdatedAt() != null ? user.getUpdatedAt() : ""
            ));
        } else {
            response.put("authenticated", false);
        }
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/profile")
    public ResponseEntity<Map<String, Object>> updateProfile(@RequestBody Map<String, String> profileData,
                                                             HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Map<String, Object> response = new HashMap<>();
        
        if (session != null && session.getAttribute("user") != null) {
            User user = (User) session.getAttribute("user");
            
            // Update user profile
            if (profileData.containsKey("displayName")) {
                user.setDisplayName(profileData.get("displayName"));
            }
            if (profileData.containsKey("bio")) {
                user.setBio(profileData.get("bio"));
            }
            
            User updatedUser = userService.save(user);
            session.setAttribute("user", updatedUser);
            
            response.put("success", true);
            response.put("user", Map.of(
                "id", updatedUser.getId(),
                "email", updatedUser.getEmail(),
                "displayName", updatedUser.getDisplayName(),
                "avatarUrl", updatedUser.getAvatarUrl() != null ? updatedUser.getAvatarUrl() : "",
                "bio", updatedUser.getBio() != null ? updatedUser.getBio() : "",
                // --- FIX 2 ---
                "createdAt", updatedUser.getCreatedAt() != null ? updatedUser.getCreatedAt() : "",
                "updatedAt", updatedUser.getUpdatedAt() != null ? updatedUser.getUpdatedAt() : ""
            ));
        } else {
            response.put("success", false);
            response.put("error", "User not authenticated");
        }
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Logged out successfully");
        
        return ResponseEntity.ok(response);
    }
}
