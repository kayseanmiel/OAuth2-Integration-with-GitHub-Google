package com.example.oauth2app.security;

import com.example.oauth2app.entity.AuthProvider;
import com.example.oauth2app.entity.User;
import com.example.oauth2app.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    
    @Autowired
    private UserService userService;
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                      Authentication authentication) throws IOException, ServletException {
        
        try {
            OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
            String registrationId = getRegistrationId(request);
            
            System.out.println("OAuth2 Success - Registration ID: " + registrationId);
            System.out.println("OAuth2 User attributes: " + oauth2User.getAttributes());
            
            User user = null;
            
            if ("google".equals(registrationId)) {
                user = processGoogleUser(oauth2User);
            } else if ("github".equals(registrationId)) {
                user = processGitHubUser(oauth2User);
            } else {
                System.err.println("Unknown registration ID: " + registrationId);
                response.sendRedirect("http://localhost:3000/?error=unknown_provider");
                return;
            }
            
            if (user != null) {
                // Store user info in session
                request.getSession().setAttribute("user", user);
                System.out.println("User created/found: " + user.getEmail());
                response.sendRedirect("http://localhost:3000/profile");
            } else {
                System.err.println("Failed to create/find user");
                response.sendRedirect("http://localhost:3000/?error=user_creation_failed");
            }
        } catch (Exception e) {
            System.err.println("OAuth2 authentication error: " + e.getMessage());
            e.printStackTrace();
            
            // Log more specific error information
            if (e.getCause() != null) {
                System.err.println("Caused by: " + e.getCause().getMessage());
                e.getCause().printStackTrace();
            }
            
            response.sendRedirect("http://localhost:3000/?error=authentication_failed");
        }
    }
    
    private String getRegistrationId(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        System.out.println("Request URI: " + requestURI);
        
        // Check for Google OAuth2 callback
        if (requestURI.contains("/login/oauth2/code/google")) {
            return "google";
        }
        // Check for GitHub OAuth2 callback
        else if (requestURI.contains("/login/oauth2/code/github")) {
            return "github";
        }
        // Fallback: check authorization URLs
        else if (requestURI.contains("/oauth2/authorization/google")) {
            return "google";
        }
        else if (requestURI.contains("/oauth2/authorization/github")) {
            return "github";
        }
        
        System.err.println("Could not determine registration ID from URI: " + requestURI);
        return null;
    }
    
    private User processGoogleUser(OAuth2User oauth2User) {
        try {
            String email = oauth2User.getAttribute("email");
            String name = oauth2User.getAttribute("name");
            String picture = oauth2User.getAttribute("picture");
            String id = oauth2User.getAttribute("sub");
            
            System.out.println("Google user - Email: " + email + ", Name: " + name + ", ID: " + id);
            
            if (email == null || id == null) {
                System.err.println("Missing required Google user attributes");
                return null;
            }
            
            return userService.findOrCreateUser(
                email, name, picture, 
                AuthProvider.Provider.GOOGLE, id, email
            );
        } catch (Exception e) {
            System.err.println("Error processing Google user: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    private User processGitHubUser(OAuth2User oauth2User) {
        try {
            String email = oauth2User.getAttribute("email");
            String name = oauth2User.getAttribute("name");
            String avatarUrl = oauth2User.getAttribute("avatar_url");
            Object idObj = oauth2User.getAttribute("id");
            String id = idObj != null ? idObj.toString() : null;
            
            System.out.println("GitHub user - Email: " + email + ", Name: " + name + ", ID: " + id);
            
            if (id == null) {
                System.err.println("Missing required GitHub user ID");
                return null;
            }
            
            return userService.findOrCreateUser(
                email, name, avatarUrl, 
                AuthProvider.Provider.GITHUB, id, email
            );
        } catch (Exception e) {
            System.err.println("Error processing GitHub user: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
