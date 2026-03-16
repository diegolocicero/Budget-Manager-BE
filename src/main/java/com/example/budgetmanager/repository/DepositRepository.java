package com.example.budgetmanager.repository;

import com.example.budgetmanager.model.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
 
import java.util.List;
import java.util.UUID;
 
@Repository
public interface DepositRepository extends JpaRepository<Deposit, Long> {

    List<Deposit> findByUserId(UUID userId);
 
    List<Deposit> findByLabel(String label);
 
    @Query("SELECT COALESCE(SUM(e.value), 0) FROM Entrate e")
    Long sumAllValues();
}
 