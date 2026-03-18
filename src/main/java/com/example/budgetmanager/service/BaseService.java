package com.example.budgetmanager.service;

import com.example.budgetmanager.model.User;
import com.example.budgetmanager.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.OffsetDateTime;
import java.util.UUID;

public abstract class BaseService {

    protected final UserRepository userRepository;

    protected BaseService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    protected UUID getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return UUID.fromString(auth.getName());
    }

    protected User getOrCreateUser(UUID userId) {
        return userRepository.findById(userId).orElseGet(() -> {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            String email = "";
            String username = userId.toString();

            if (auth.getPrincipal() instanceof Jwt jwt) {
                email = jwt.getClaimAsString("email") != null ? jwt.getClaimAsString("email") : "";
                username = jwt.getClaimAsString("email") != null ? jwt.getClaimAsString("email") : userId.toString();
            }

            User newUser = new User();
            newUser.setId(userId);
            newUser.setUsername(username);
            newUser.setEmail(email);
            newUser.setCreatedAt(OffsetDateTime.now());
            return userRepository.save(newUser);
        });
    }
}