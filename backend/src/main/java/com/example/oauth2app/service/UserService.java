package com.example.oauth2app.service;

import com.example.oauth2app.entity.AuthProvider;
import com.example.oauth2app.entity.User;
import com.example.oauth2app.repository.AuthProviderRepository;
import com.example.oauth2app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AuthProviderRepository authProviderRepository;
    
    public User findOrCreateUser(String email, String displayName, String avatarUrl, 
                                AuthProvider.Provider provider, String providerUserId, String providerEmail) {
        System.out.println("UserService.findOrCreateUser called with email: " + email + ", provider: " + provider);
        
        // First, try to find existing user by email
        Optional<User> existingUser = userRepository.findByEmail(email);
        
        if (existingUser.isPresent()) {
            User user = existingUser.get();
            // Check if this provider is already linked to this user
            Optional<AuthProvider> existingProvider = authProviderRepository
                .findByProviderAndProviderUserId(provider, providerUserId);
            
            if (existingProvider.isEmpty()) {
                // Link new provider to existing user
                AuthProvider authProvider = new AuthProvider(user, provider, providerUserId, providerEmail);
                user.addAuthProvider(authProvider);
                authProviderRepository.save(authProvider);
            }
            
            // Update user info if needed
            if (user.getDisplayName() == null || user.getDisplayName().isEmpty()) {
                user.setDisplayName(displayName);
            }
            if (user.getAvatarUrl() == null || user.getAvatarUrl().isEmpty()) {
                user.setAvatarUrl(avatarUrl);
            }
            
            return userRepository.save(user);
        } else {
            // Create new user
            User newUser = new User(email, displayName);
            newUser.setAvatarUrl(avatarUrl);
            User savedUser = userRepository.save(newUser);
            
            // Create auth provider
            AuthProvider authProvider = new AuthProvider(savedUser, provider, providerUserId, providerEmail);
            savedUser.addAuthProvider(authProvider);
            authProviderRepository.save(authProvider);
            
            return savedUser;
        }
    }
    
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    public User save(User user) {
        return userRepository.save(user);
    }
    
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
}
