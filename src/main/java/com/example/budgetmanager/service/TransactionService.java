package com.example.budgetmanager.service;

import com.example.budgetmanager.dto.TransactionDTO;
import com.example.budgetmanager.model.Deposit;
import com.example.budgetmanager.model.Withdrawal;
import com.example.budgetmanager.repository.DepositRepository;
import com.example.budgetmanager.repository.WithdrawalRepository;
import com.example.budgetmanager.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
@Transactional(readOnly = true)
public class TransactionService extends BaseService {

    private final DepositRepository depositRepository;
    private final WithdrawalRepository withdrawalRepository;

    public TransactionService(DepositRepository depositRepository,
                              WithdrawalRepository withdrawalRepository,
                              UserRepository userRepository) {
        super(userRepository);
        this.depositRepository = depositRepository;
        this.withdrawalRepository = withdrawalRepository;
    }

    public Page<TransactionDTO.Response> getAll(Pageable pageable, TransactionDTO.Type type, String label) {
        var userId = getCurrentUserId();

        Stream<TransactionDTO.Response> stream;

        // Recupero e trasformazione in base al tipo, applicando il filtro label se presente
        if (type == TransactionDTO.Type.DEPOSIT) {
            stream = filterByLabel(depositRepository.findByUserId(userId).stream(), label).map(this::toResponse);
        } else if (type == TransactionDTO.Type.WITHDRAWAL) {
            stream = filterByLabel(withdrawalRepository.findByUserId(userId).stream(), label).map(this::toResponse);
        } else {
            stream = Stream.concat(
                filterByLabel(depositRepository.findByUserId(userId).stream(), label).map(this::toResponse),
                filterByLabel(withdrawalRepository.findByUserId(userId).stream(), label).map(this::toResponse)
            );
        }

        // Ordinamento per data decrescente
        List<TransactionDTO.Response> sorted = stream
                .sorted(Comparator.comparing(TransactionDTO.Response::getCreatedAt).reversed())
                .toList();

        // Paginazione manuale sulla lista filtrata e ordinata
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), sorted.size());
        
        List<TransactionDTO.Response> pageContent = (start >= sorted.size())
                ? List.of()
                : sorted.subList(start, end);

        return new PageImpl<>(pageContent, pageable, sorted.size());
    }

    // Metodo helper per filtrare lo stream in base alla label (case-insensitive)
    private <T> Stream<T> filterByLabel(Stream<T> stream, String label) {
        if (label == null || label.isBlank()) {
            return stream;
        }
        String lowerLabel = label.toLowerCase();
        return stream.filter(t -> {
            String itemLabel = (t instanceof Deposit) ? ((Deposit) t).getLabel() : ((Withdrawal) t).getLabel();
            return itemLabel != null && itemLabel.toLowerCase().contains(lowerLabel);
        });
    }

    private TransactionDTO.Response toResponse(Deposit d) {
        return new TransactionDTO.Response(
                d.getId(), d.getValue(), d.getLabel(), d.getCreatedAt(), TransactionDTO.Type.DEPOSIT);
    }

    private TransactionDTO.Response toResponse(Withdrawal w) {
        return new TransactionDTO.Response(
                w.getId(), w.getValue(), w.getLabel(), w.getCreatedAt(), TransactionDTO.Type.WITHDRAWAL);
    }
}