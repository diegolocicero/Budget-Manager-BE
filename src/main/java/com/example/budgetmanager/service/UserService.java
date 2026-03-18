package com.example.budgetmanager.service;

import com.example.budgetmanager.model.User;
import com.example.budgetmanager.dto.UserDTO;
import com.example.budgetmanager.repository.UserRepository;
import com.example.budgetmanager.exceptionHandler.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class UserService extends BaseService {

    public UserService(UserRepository userRepository) {
        super(userRepository);
    }

    public List<UserDTO.Response> getAll() {
        return userRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public UserDTO.Response getById(UUID id) {
        return toResponse(getOrThrow(id));
    }

    @Transactional
    public UserDTO.Response create(UserDTO.Request request) {
        User user = new User(request.getUsername(), request.getEmail());
        return toResponse(userRepository.save(user));
    }

    @Transactional
    public UserDTO.Response update(UUID id, UserDTO.Request request) {
        User user = getOrThrow(id);
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        return toResponse(userRepository.save(user));
    }

    @Transactional
    public void delete(UUID id) {
        getOrThrow(id);
        userRepository.deleteById(id);
    }

    private User getOrThrow(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
    }

    private UserDTO.Response toResponse(User u) {
        return new UserDTO.Response(u.getId(), u.getUsername(), u.getEmail(), u.getCreatedAt());
    }
}