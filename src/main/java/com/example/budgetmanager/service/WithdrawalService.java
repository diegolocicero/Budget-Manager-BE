package com.example.budgetmanager.service;

import com.example.budgetmanager.dto.WithdrawalDTO;
import com.example.budgetmanager.model.Withdrawal;
import com.example.budgetmanager.repository.WithdrawalRepository;
import com.example.budgetmanager.repository.UserRepository;
import com.example.budgetmanager.exceptionHandler.ResourceNotFoundException;
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

    public List<WithdrawalDTO.Response> getAll() {
        return withdrawalRepository.findByUserId(getCurrentUserId()).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public WithdrawalDTO.Response getById(Long id) {
        Withdrawal withdrawal = getOrThrow(id);
        validateOwnership(withdrawal.getUser().getId());
        return toResponse(withdrawal);
    }

    @Transactional
    public WithdrawalDTO.Response create(WithdrawalDTO.Request request) {
        Withdrawal withdrawal = new Withdrawal(request.getValue(), request.getLabel(), getOrCreateUser(getCurrentUserId()));
        return toResponse(withdrawalRepository.save(withdrawal));
    }

    @Transactional
    public WithdrawalDTO.Response update(Long id, WithdrawalDTO.Request request) {
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

    private void validateOwnership(UUID ownerId) {
        if (!ownerId.equals(getCurrentUserId())) {
            throw new ResourceNotFoundException("Withdrawal", ownerId);
        }
    }

    private Withdrawal getOrThrow(Long id) {
        return withdrawalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Withdrawal", id));
    }

    private WithdrawalDTO.Response toResponse(Withdrawal w) {
        return new WithdrawalDTO.Response(w.getId(), w.getValue(), w.getLabel(), w.getCreatedAt());
    }
}