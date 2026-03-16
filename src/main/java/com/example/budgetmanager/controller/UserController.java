package com.example.budgetmanager.controller;

import com.example.budgetmanager.dto.UserDTO;
import com.example.budgetmanager.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO.Response> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getById(id));
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
            @PathVariable Long id, 
            @Valid @RequestBody UserDTO.Request request) {
        return ResponseEntity.ok(userService.update(id, request));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}