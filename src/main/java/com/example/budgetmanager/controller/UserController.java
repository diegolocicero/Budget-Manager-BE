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
    @PutMapping("/me")
    public ResponseEntity<UserDTO.Response> updateMe(@Valid @RequestBody UserDTO.Request request) {
        return ResponseEntity.ok(userService.updateMe(request));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // DA CHIAMARE DOPO IL LOGIN A FRONTEND PER CREARE L'UTENTE ANCHE NELLA TABELLA
    // UTENTI DEL DB PUBLIC DI SUPABASE
    @PostMapping("/sync")
    public ResponseEntity<Void> sync() {
        userService.syncCurrentUser();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<UserDTO.Response> getMe() {
        return ResponseEntity.ok(userService.getMe());
    }
}