package com.example.budgetmanager.service;

import com.example.budgetmanager.dto.WithdrawalDTO;
import com.example.budgetmanager.model.Withdrawal;
import com.example.budgetmanager.repository.WithdrawalRepository;
import com.example.budgetmanager.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
 
import java.util.List;
 
@Service
@Transactional(readOnly = true)
public class WithdrawalService {
 
    private final WithdrawalRepository withdrawalRepository;
 
    public WithdrawalService(WithdrawalRepository withdrawalRepository) {
        this.withdrawalRepository = withdrawalRepository;
    }
 
    public List<WithdrawalDTO.Response> getAll() {
        return withdrawalRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }
 
    public WithdrawalDTO.Response getById(Long id) {
        return toResponse(getOrThrow(id));
    }
 
    @Transactional
    public WithdrawalDTO.Response create(WithdrawalDTO.Request request) {
        Withdrawal withdrawal = new Withdrawal(request.getValue(), request.getLabel());
        return toResponse(withdrawalRepository.save(withdrawal));
    }
 
    @Transactional
    public WithdrawalDTO.Response update(Long id, WithdrawalDTO.Request request) {
        Withdrawal withdrawal = getOrThrow(id);
        withdrawal.setValue(request.getValue());
        withdrawal.setLabel(request.getLabel());
        return toResponse(withdrawalRepository.save(withdrawal));
    }
 
    @Transactional
    public void delete(Long id) {
        getOrThrow(id);
        withdrawalRepository.deleteById(id);
    }
  
    private Withdrawal getOrThrow(Long id) {
        return withdrawalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Withdrawal", id));
    }
 
    private WithdrawalDTO.Response toResponse(Withdrawal w) {
        return new WithdrawalDTO.Response(w.getId(), w.getValue(), w.getLabel(), w.getCreatedAt());
    }
}
 