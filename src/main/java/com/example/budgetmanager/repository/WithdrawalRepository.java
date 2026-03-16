package com.example.budgetmanager.repository;

import com.example.budgetmanager.model.Withdrawal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface WithdrawalRepository extends JpaRepository<Withdrawal, Long> {

    List<Withdrawal> findByUserId(UUID userId);

    List<Withdrawal> findByUserIdAndLabel(UUID userId, String label);

    @Query("SELECT COALESCE(SUM(w.value), 0) FROM Withdrawal w WHERE w.user.id = :userId")
    Long sumValuesByUserId(@Param("userId") UUID userId);
}