package com.example.budgetmanager.service;

import com.example.budgetmanager.dto.DepositDTO;
import com.example.budgetmanager.model.Deposit;
import com.example.budgetmanager.model.User;
import com.example.budgetmanager.repository.DepositRepository;
import com.example.budgetmanager.repository.UserRepository;
import com.example.budgetmanager.exceptionHandler.ResourceNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class DepositService {

    private final DepositRepository depositRepository;
    private final UserRepository userRepository;

    public DepositService(DepositRepository depositRepository, UserRepository userRepository) {
        this.depositRepository = depositRepository;
        this.userRepository = userRepository;
    }

    public List<DepositDTO.Response> getAll() {
        UUID userId = getCurrentUserId();
        return depositRepository.findByUserId(userId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public DepositDTO.Response getById(Long id) {
        Deposit deposit = getOrThrow(id);
        if (!deposit.getUser().getId().equals(Long.valueOf(getCurrentUserId().toString()))) {
            throw new ResourceNotFoundException("Deposit", id);
        }
        return toResponse(deposit);
    }

    @Transactional
    public DepositDTO.Response create(DepositDTO.Request request) {
        User user = userRepository.findById(getCurrentUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Deposit deposit = new Deposit(request.getValue(), request.getLabel(), user);
        return toResponse(depositRepository.save(deposit));
    }

    @Transactional
    public DepositDTO.Response update(Long id, DepositDTO.Request request) {
        Deposit deposit = getOrThrow(id);
        if (!deposit.getUser().getId().equals(Long.valueOf(getCurrentUserId().toString()))) {
            throw new ResourceNotFoundException("Deposit", id);
        }
        
        deposit.setValue(request.getValue());
        deposit.setLabel(request.getLabel());
        return toResponse(depositRepository.save(deposit));
    }

    @Transactional
    public void delete(Long id) {
        Deposit deposit = getOrThrow(id);
        if (!deposit.getUser().getId().equals(Long.valueOf(getCurrentUserId().toString()))) {
            throw new ResourceNotFoundException("Deposit", id);
        }
        depositRepository.deleteById(id);
    }

    private UUID getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return UUID.fromString(auth.getName());
    }

    private Deposit getOrThrow(Long id) {
        return depositRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deposit", id));
    }

    private DepositDTO.Response toResponse(Deposit d) {
        return new DepositDTO.Response(d.getId(), d.getValue(), d.getLabel(), d.getCreatedAt());
    }
}