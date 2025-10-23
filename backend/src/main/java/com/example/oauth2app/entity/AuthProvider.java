package com.example.oauth2app.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "auth_providers")
public class AuthProvider {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull
    private User user;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private Provider provider;
    
    @NotBlank
    @Column(name = "provider_user_id", nullable = false)
    private String providerUserId;
    
    @Email
    @Column(name = "provider_email")
    private String providerEmail;
    
    // Constructors
    public AuthProvider() {}
    
    public AuthProvider(User user, Provider provider, String providerUserId, String providerEmail) {
        this.user = user;
        this.provider = provider;
        this.providerUserId = providerUserId;
        this.providerEmail = providerEmail;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public Provider getProvider() {
        return provider;
    }
    
    public void setProvider(Provider provider) {
        this.provider = provider;
    }
    
    public String getProviderUserId() {
        return providerUserId;
    }
    
    public void setProviderUserId(String providerUserId) {
        this.providerUserId = providerUserId;
    }
    
    public String getProviderEmail() {
        return providerEmail;
    }
    
    public void setProviderEmail(String providerEmail) {
        this.providerEmail = providerEmail;
    }
    
    // Provider enum
    public enum Provider {
        GOOGLE, GITHUB
    }
}
