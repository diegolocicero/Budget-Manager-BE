package com.example.budgetmanager.service;

import com.example.budgetmanager.dto.TransactionDTO;
import com.example.budgetmanager.model.Withdrawal;
import com.example.budgetmanager.repository.WithdrawalRepository;
import com.example.budgetmanager.repository.UserRepository;
import com.example.budgetmanager.exceptionHandler.ResourceNotFoundException;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class WithdrawalService extends BaseService {

    private final WithdrawalRepository withdrawalRepository;

    public WithdrawalService(WithdrawalRepository withdrawalRepository, UserRepository userRepository) {
        super(userRepository);
        this.withdrawalRepository = withdrawalRepository;
    }

    public List<TransactionDTO.Response> getAll() {
        return withdrawalRepository.findByUserId(getCurrentUserId()).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public TransactionDTO.Response getById(Long id) {
        Withdrawal withdrawal = getOrThrow(id);
        validateOwnership(withdrawal.getUser().getId());
        return toResponse(withdrawal);
    }

    @Transactional
    public TransactionDTO.Response create(TransactionDTO.Request request) {
        Withdrawal withdrawal = new Withdrawal(request.getValue(), request.getLabel(), getOrCreateUser(getCurrentUserId()));
        return toResponse(withdrawalRepository.save(withdrawal));
    }

    @Transactional
    public TransactionDTO.Response update(Long id, TransactionDTO.Request request) {
        Withdrawal withdrawal = getOrThrow(id);
        validateOwnership(withdrawal.getUser().getId());
        withdrawal.setValue(request.getValue());
        withdrawal.setLabel(request.getLabel());
        return toResponse(withdrawalRepository.save(withdrawal));
    }

    @Transactional
    public void delete(Long id) {
        Withdrawal withdrawal = getOrThrow(id);
        validateOwnership(withdrawal.getUser().getId());
        withdrawalRepository.deleteById(id);
    }

    public List<TransactionDTO.Response> getRecent(int limit) {
    return withdrawalRepository
        .findByUserIdOrderByCreatedAtDesc(getCurrentUserId(), PageRequest.of(0, limit))
        .stream()
        .map(this::toResponse)
        .collect(Collectors.toList());
}

    private void validateOwnership(UUID ownerId) {
        if (!ownerId.equals(getCurrentUserId())) {
            throw new ResourceNotFoundException("Withdrawal", ownerId);
        }
    }

    private Withdrawal getOrThrow(Long id) {
        return withdrawalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Withdrawal", id));
    }

    private TransactionDTO.Response toResponse(Withdrawal w) {
        return new TransactionDTO.Response(w.getId(), w.getValue(), w.getLabel(), w.getCreatedAt(), TransactionDTO.Type.WITHDRAWAL);
    }
}