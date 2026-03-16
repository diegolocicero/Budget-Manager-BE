package com.example.budgetmanager.model;

import jakarta.persistence.*;

@Entity
@Table(name = "uscite")
public class Withdrawal extends BaseModel {

    @Column(name = "value", nullable = false)
    private Long value;

    @Column(name = "label")
    private String label;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Withdrawal() {}

    public Withdrawal(Long value, String label, User user) {
        this.value = value;
        this.label = label;
        this.user = user;
    }

    // Getter e Setter
    public Long getValue() { return value; }
    public void setValue(Long value) { this.value = value; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}