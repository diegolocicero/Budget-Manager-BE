package com.example.budgetmanager.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;
 
@MappedSuperclass
public abstract class BaseModel {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;
 
    @PrePersist
    protected void onCreate() {
        this.createdAt = OffsetDateTime.now();
    }
 
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
 
    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
}