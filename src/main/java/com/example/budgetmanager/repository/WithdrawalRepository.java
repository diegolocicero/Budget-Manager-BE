package com.example.budgetmanager.repository;

import com.example.budgetmanager.model.Withdrawal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
 
import java.util.List;
 
@Repository
public interface WithdrawalRepository extends JpaRepository<Withdrawal, Long> {
 
    List<Withdrawal> findByLabel(String label);
 
    @Query("SELECT COALESCE(SUM(u.value), 0) FROM Uscite u")
    Long sumAllValues();
}
 