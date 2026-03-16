package com.example.budgetmanager.service;

import com.example.budgetmanager.model.User;
import com.example.budgetmanager.dto.UserDTO;
import com.example.budgetmanager.repository.UserRepository;
import com.example.budgetmanager.exception.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
 
import java.util.List;
 
@Service
@Transactional(readOnly = true)
public class UserService {
 
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
 
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
 
    public List<UserDTO.Response> getAll() {
        return userRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }
 
    public UserDTO.Response getById(Long id) {
        return toResponse(getOrThrow(id));
    }
 
    @Transactional
    public UserDTO.Response create(UserDTO.Request request) {
        User user = new User(
                request.getUsername(),
                passwordEncoder.encode(request.getPassword()),
                request.getEmail()
        );
        return toResponse(userRepository.save(user));
    }
 
    @Transactional
    public UserDTO.Response update(Long id, UserDTO.Request request) {
        User user = getOrThrow(id);
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        return toResponse(userRepository.save(user));
    }
 
    @Transactional
    public void delete(Long id) {
        getOrThrow(id);
        userRepository.deleteById(id);
    }
 
 
    private User getOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
    }
 
    private UserDTO.Response toResponse(User u) {
        return new UserDTO.Response(u.getId(), u.getUsername(), u.getEmail(), u.getCreatedAt());
    }
}