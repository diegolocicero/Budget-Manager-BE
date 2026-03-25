package com.example.budgetmanager.service;

import com.example.budgetmanager.dto.TransactionDTO;
import com.example.budgetmanager.model.Deposit;
import com.example.budgetmanager.repository.DepositRepository;
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
public class DepositService extends BaseService {

    private final DepositRepository depositRepository;

    public DepositService(DepositRepository depositRepository, UserRepository userRepository) {
        super(userRepository);
        this.depositRepository = depositRepository;
    }

    public List<TransactionDTO.Response> getAll() {
        return depositRepository.findByUserId(getCurrentUserId()).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public TransactionDTO.Response getById(Long id) {
        Deposit deposit = getOrThrow(id);
        validateOwnership(deposit.getUser().getId());
        return toResponse(deposit);
    }

    @Transactional
    public TransactionDTO.Response create(TransactionDTO.Request request) {
        Deposit deposit = new Deposit(request.getValue(), request.getLabel(), getOrCreateUser(getCurrentUserId()));
        return toResponse(depositRepository.save(deposit));
    }

    @Transactional
    public TransactionDTO.Response update(Long id, TransactionDTO.Request request) {
        Deposit deposit = getOrThrow(id);
        validateOwnership(deposit.getUser().getId());
        deposit.setValue(request.getValue());
        deposit.setLabel(request.getLabel());
        return toResponse(depositRepository.save(deposit));
    }

    @Transactional
    public void delete(Long id) {
        Deposit deposit = getOrThrow(id);
        validateOwnership(deposit.getUser().getId());
        depositRepository.deleteById(id);
    }

    public List<TransactionDTO.Response> getRecent(int limit) {
        return depositRepository
                .findByUserIdOrderByCreatedAtDesc(getCurrentUserId(), PageRequest.of(0, limit))
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private void validateOwnership(UUID ownerId) {
        if (!ownerId.equals(getCurrentUserId())) {
            throw new ResourceNotFoundException("Deposit", ownerId);
        }
    }

    private Deposit getOrThrow(Long id) {
        return depositRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Deposit", id));
    }

    private TransactionDTO.Response toResponse(Deposit d) {
        return new TransactionDTO.Response(d.getId(), d.getValue(), d.getLabel(), d.getCreatedAt(), TransactionDTO.Type.DEPOSIT);
    }
}