package com.example.budgetmanager.service;

import com.example.budgetmanager.dto.DepositDTO;
import com.example.budgetmanager.model.Deposit;
import com.example.budgetmanager.repository.DepositRepository;
import com.example.budgetmanager.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
 
import java.util.List;
 
@Service
@Transactional(readOnly = true)
public class DepositService {
 
    private final DepositRepository depositRepository;
 
    public DepositService(DepositRepository depositRepository) {
        this.depositRepository = depositRepository;
    }
 
    public List<DepositDTO.Response> getAll() {
        return depositRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }
 
    public DepositDTO.Response getById(Long id) {
        return toResponse(getOrThrow(id));
    }
 
    @Transactional
    public DepositDTO.Response create(DepositDTO.Request request) {
        Deposit deposit = new Deposit(request.getValue(), request.getLabel());
        return toResponse(depositRepository.save(deposit));
    }
 
    @Transactional
    public DepositDTO.Response update(Long id, DepositDTO.Request request) {
        Deposit deposit = getOrThrow(id);
        deposit.setValue(request.getValue());
        deposit.setLabel(request.getLabel());
        return toResponse(depositRepository.save(deposit));
    }
 
    @Transactional
    public void delete(Long id) {
        getOrThrow(id);
        depositRepository.deleteById(id);
    }
 
 
    private Deposit getOrThrow(Long id) {
        return depositRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deposit", id));
    }
 
    private DepositDTO.Response toResponse(Deposit d) {
        return new DepositDTO.Response(d.getId(), d.getValue(), d.getLabel(), d.getCreatedAt());
    }
}
 
