package com.example.budgetmanager.model;

import jakarta.persistence.*;

@Entity
@Table(name = "entrate")
public class Deposit extends BaseModel {

    @Column(name = "value", nullable = false)
    private Long value;

    @Column(name = "label")
    private String label;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Deposit() {}

    public Deposit(Long value, String label, User user) {
        this.value = value;
        this.label = label;
        this.user = user;
    }

    public Long getValue() { return value; }
    public void setValue(Long value) { this.value = value; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}