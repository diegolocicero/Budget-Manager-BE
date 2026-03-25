package com.example.budgetmanager.service;

import com.example.budgetmanager.dto.TransactionDTO;
import com.example.budgetmanager.model.Deposit;
import com.example.budgetmanager.model.Withdrawal;
import com.example.budgetmanager.repository.DepositRepository;
import com.example.budgetmanager.repository.WithdrawalRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
public class TransactionService {

    private final DepositRepository depositRepository;
    private final WithdrawalRepository withdrawalRepository;

    public TransactionService(DepositRepository depositRepository,
                              WithdrawalRepository withdrawalRepository) {
        this.depositRepository = depositRepository;
        this.withdrawalRepository = withdrawalRepository;
    }

    public Page<TransactionDTO.Response> getAll(Pageable pageable, TransactionDTO.Type type) {

        Stream<TransactionDTO.Response> stream = switch (type == null ? TransactionDTO.Type.valueOf("ALL") : type) {
            case DEPOSIT -> depositRepository.findAll().stream()
                    .map(this::toResponse);
            case WITHDRAWAL -> withdrawalRepository.findAll().stream()
                    .map(this::toResponse);
            default -> Stream.concat(
                    depositRepository.findAll().stream().map(this::toResponse),
                    withdrawalRepository.findAll().stream().map(this::toResponse)
            );
        };

        // Ordina per createdAt DESC e applica paginazione in memoria
        List<TransactionDTO.Response> sorted = stream
                .sorted(Comparator.comparing(TransactionDTO.Response::getCreatedAt).reversed())
                .toList();

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), sorted.size());
        List<TransactionDTO.Response> pageContent = (start >= sorted.size())
                ? List.of()
                : sorted.subList(start, end);

        return new PageImpl<>(pageContent, pageable, sorted.size());
    }

    private TransactionDTO.Response toResponse(Deposit d) {
        return new TransactionDTO.Response(
                d.getId(),
                d.getValue(),
                d.getLabel(),
                d.getCreatedAt(),
                TransactionDTO.Type.DEPOSIT
        );
    }

    private TransactionDTO.Response toResponse(Withdrawal w) {
        return new TransactionDTO.Response(
                w.getId(),
                w.getValue(),
                w.getLabel(),
                w.getCreatedAt(),
                TransactionDTO.Type.WITHDRAWAL
        );
    }
}