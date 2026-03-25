package com.example.budgetmanager.controller;

import com.example.budgetmanager.dto.TransactionDTO;
import com.example.budgetmanager.service.WithdrawalService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/withdrawals")
public class WithdrawalController {

    private final WithdrawalService withdrawalService;

    public WithdrawalController(WithdrawalService withdrawalService) {
        this.withdrawalService = withdrawalService;
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<TransactionDTO.Response>> getAll() {
        return ResponseEntity.ok(withdrawalService.getAll());
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<TransactionDTO.Response> getById(@PathVariable Long id) {
        return ResponseEntity.ok(withdrawalService.getById(id));
    }

    // POST (CREATE)
    @PostMapping
    public ResponseEntity<TransactionDTO.Response> create(@Valid @RequestBody TransactionDTO.Request request) {
        TransactionDTO.Response createdWithdrawal = withdrawalService.create(request);
        return new ResponseEntity<>(createdWithdrawal, HttpStatus.CREATED);
    }

    // PUT (UPDATE)
    @PutMapping("/{id}")
    public ResponseEntity<TransactionDTO.Response> update(
            @PathVariable Long id,
            @Valid @RequestBody TransactionDTO.Request request) {
        return ResponseEntity.ok(withdrawalService.update(id, request));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        withdrawalService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // GET LAST 5 WITHDRAWALS BY ID ORDERED BY CREATED_AT DESC
    @GetMapping("/recent")
    public ResponseEntity<List<TransactionDTO.Response>> getRecent(
            @RequestParam(defaultValue = "5") int limit) {
        return ResponseEntity.ok(withdrawalService.getRecent(limit));
    }
}