package com.example.budgetmanager.repository;

import com.example.budgetmanager.model.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

@Repository
public interface DepositRepository extends JpaRepository<Deposit, Long> {

    List<Deposit> findByUserId(UUID userId);

    List<Deposit> findByLabel(String label);

    List<Deposit> findByUserIdOrderByCreatedAtDesc(UUID userId, Pageable pageable);

    @Query("SELECT COALESCE(SUM(d.value), 0) FROM Deposit d WHERE d.user.id = :userId")
    Long sumValuesByUserId(@Param("userId") UUID userId);
}
