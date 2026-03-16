package com.example.budgetmanager.repository;

import com.example.budgetmanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
 
import java.util.Optional;
import java.util.UUID;
 
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
 
    Optional<User> findByUsername(String username);
 
    Optional<User> findByEmail(String email);

    Optional<User> findById(UUID id);
 
    boolean existsByUsername(String username);
 
    boolean existsByEmail(String email);
}