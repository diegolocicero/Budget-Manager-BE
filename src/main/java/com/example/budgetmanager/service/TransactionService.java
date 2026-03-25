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

    /**
     * Recupera tutte le transazioni (Entrate e Uscite) dell'utente corrente,
     * applicando filtri per tipo e ricerca testuale sulla label.
     */
    public Page<TransactionDTO.Response> getAll(Pageable pageable, TransactionDTO.Type type, String label) {
        var userId = getCurrentUserId();

        // 1. Inizializziamo lo stream in base al tipo richiesto
        Stream<TransactionDTO.Response> stream;

        if (type == TransactionDTO.Type.DEPOSIT) {
            stream = depositRepository.findByUserId(userId).stream()
                    .map(this::toResponse);
        } else if (type == TransactionDTO.Type.WITHDRAWAL) {
            stream = withdrawalRepository.findByUserId(userId).stream()
                    .map(this::toResponse);
        } else {
            // Unione di entrambi i flussi se il tipo è null (Tutti)
            stream = Stream.concat(
                depositRepository.findByUserId(userId).stream().map(this::toResponse),
                withdrawalRepository.findByUserId(userId).stream().map(this::toResponse)
            );
        }

        // 2. Applichiamo il filtro testuale (Case-Insensitive) se la label è presente
        if (label != null && !label.isBlank()) {
            String searchTag = label.toLowerCase().trim();
            stream = stream.filter(t -> 
                t.getLabel() != null && t.getLabel().toLowerCase().contains(searchTag)
            );
        }

        // 3. Trasformiamo lo stream in una lista ordinata per data (decrescente)
        // Nota: Poiché uniamo due tabelle diverse, l'ordinamento va fatto in memoria
        List<TransactionDTO.Response> allResults = stream
                .sorted(Comparator.comparing(TransactionDTO.Response::getCreatedAt).reversed())
                .toList();

        // 4. Gestione della paginazione manuale sulla lista finale
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), allResults.size());

        List<TransactionDTO.Response> pageContent;
        if (start > allResults.size()) {
            pageContent = List.of();
        } else {
            pageContent = allResults.subList(start, end);
        }

        return new PageImpl<>(pageContent, pageable, allResults.size());
    }

    // --- MAPPERS ---

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