package com.example.oauth2app.repository;

import com.example.oauth2app.entity.AuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthProviderRepository extends JpaRepository<AuthProvider, Long> {
    Optional<AuthProvider> findByProviderAndProviderUserId(AuthProvider.Provider provider, String providerUserId);
    Optional<AuthProvider> findByProviderAndProviderEmail(AuthProvider.Provider provider, String providerEmail);
}
