package com.example.budgetmanager.controller;

import com.example.budgetmanager.dto.UserDTO;
import com.example.budgetmanager.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<UserDTO.Response>> getAllUsers() {
        return ResponseEntity.ok(userService.getAll());
    }

    // POST (CREATE)
    @PostMapping
    public ResponseEntity<UserDTO.Response> createUser(@Valid @RequestBody UserDTO.Request request) {
        UserDTO.Response createdUser = userService.create(request);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    // PUT (UPDATE)
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO.Response> updateUser(
            @PathVariable UUID id,
            @Valid @RequestBody UserDTO.Request request) {
        return ResponseEntity.ok(userService.update(id, request));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO.Response> getMe() {
        return ResponseEntity.ok(userService.getMe());
    }
}