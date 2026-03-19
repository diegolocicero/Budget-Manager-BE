package com.example.budgetmanager.controller;

import com.example.budgetmanager.dto.DepositDTO;
import com.example.budgetmanager.service.DepositService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deposits")
public class DepositController {

    private final DepositService depositService;

    public DepositController(DepositService depositService) {
        this.depositService = depositService;
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<DepositDTO.Response>> getAll() {
        return ResponseEntity.ok(depositService.getAll());
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<DepositDTO.Response> getById(@PathVariable Long id) {
        return ResponseEntity.ok(depositService.getById(id));
    }

    // POST (CREATE)
    @PostMapping
    public ResponseEntity<DepositDTO.Response> create(@Valid @RequestBody DepositDTO.Request request) {
        DepositDTO.Response createdDeposit = depositService.create(request);
        return new ResponseEntity<>(createdDeposit, HttpStatus.CREATED);
    }

    // PUT (UPDATE)
    @PutMapping("/{id}")
    public ResponseEntity<DepositDTO.Response> update(
            @PathVariable Long id,
            @Valid @RequestBody DepositDTO.Request request) {
        return ResponseEntity.ok(depositService.update(id, request));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        depositService.delete(id);
        return ResponseEntity.noContent().build();
    }

    //GET LAST 5 DEPOSITS BY ID ORDERED BY CREATED_AT DESC 
    @GetMapping("/recent")
    public ResponseEntity<List<DepositDTO.Response>> getRecent(
            @RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(depositService.getRecent(limit));
    }
}