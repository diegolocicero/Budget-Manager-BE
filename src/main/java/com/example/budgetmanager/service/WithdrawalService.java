package com.example.budgetmanager.service;

import com.example.budgetmanager.dto.WithdrawalDTO;
import com.example.budgetmanager.model.Withdrawal;
import com.example.budgetmanager.model.User;
import com.example.budgetmanager.repository.WithdrawalRepository;
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
public class WithdrawalService {

    private final WithdrawalRepository withdrawalRepository;
    private final UserRepository userRepository;

    public WithdrawalService(WithdrawalRepository withdrawalRepository, UserRepository userRepository) {
        this.withdrawalRepository = withdrawalRepository;
        this.userRepository = userRepository;
    }

    public List<WithdrawalDTO.Response> getAll() {
        UUID userId = getCurrentUserId();
        return withdrawalRepository.findByUserId(userId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public WithdrawalDTO.Response getById(Long id) {
        Withdrawal withdrawal = getOrThrow(id);
        if (!withdrawal.getUser().getId().equals(Long.valueOf(getCurrentUserId().toString()))) {
            throw new ResourceNotFoundException("Withdrawal", id);
        }
        return toResponse(withdrawal);
    }

    @Transactional
    public WithdrawalDTO.Response create(WithdrawalDTO.Request request) {
        User user = userRepository.findById(getCurrentUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Withdrawal withdrawal = new Withdrawal(request.getValue(), request.getLabel(), user);
        return toResponse(withdrawalRepository.save(withdrawal));
    }

    @Transactional
    public WithdrawalDTO.Response update(Long id, WithdrawalDTO.Request request) {
        Withdrawal withdrawal = getOrThrow(id);
        if (!withdrawal.getUser().getId().equals(getCurrentUserId().toString())) {
            throw new ResourceNotFoundException("Withdrawal", id);
        }
        
        withdrawal.setValue(request.getValue());
        withdrawal.setLabel(request.getLabel());
        return toResponse(withdrawalRepository.save(withdrawal));
    }

    @Transactional
    public void delete(Long id) {
        Withdrawal withdrawal = getOrThrow(id);
        if (!withdrawal.getUser().getId().equals(Long.valueOf(getCurrentUserId().toString()))) {
            throw new ResourceNotFoundException("Withdrawal", id);
        }
        withdrawalRepository.deleteById(id);
    }

    private UUID getCurrentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return UUID.fromString(auth.getName()); 
    }

    private Withdrawal getOrThrow(Long id) {
        return withdrawalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Withdrawal", id));
    }

    private WithdrawalDTO.Response toResponse(Withdrawal w) {
        return new WithdrawalDTO.Response(w.getId(), w.getValue(), w.getLabel(), w.getCreatedAt());
    }
}