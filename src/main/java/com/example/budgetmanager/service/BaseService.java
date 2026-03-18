package com.example.budgetmanager.service;

import com.example.budgetmanager.model.User;
import com.example.budgetmanager.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.OffsetDateTime;
import java.util.List;
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

    private static final List<String> AVATAR_URLS = List.of(
            "/avatars/avatar_1.png",
            "/avatars/avatar_2.png",
            "/avatars/avatar_3.png",
            "/avatars/avatar_4.png",
            "/avatars/avatar_5.png",
            "/avatars/avatar_6.png"
        );

    protected User getOrCreateUser(UUID userId) {
    return userRepository.findById(userId).orElseGet(() -> {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String email = "";
        if (auth.getPrincipal() instanceof Jwt jwt) {
            email = jwt.getClaimAsString("email") != null ? jwt.getClaimAsString("email") : "";
        }

        String avatarUrl = AVATAR_URLS.get(new java.util.Random().nextInt(AVATAR_URLS.size()));

        User newUser = new User();
        newUser.setId(userId);
        newUser.setUsername(email.isEmpty() ? userId.toString() : email);
        newUser.setEmail(email);
        newUser.setAvatarUrl(avatarUrl);
        newUser.setCreatedAt(OffsetDateTime.now());
        return userRepository.save(newUser);
    });
}
}