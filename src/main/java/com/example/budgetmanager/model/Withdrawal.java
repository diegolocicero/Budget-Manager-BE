package com.example.budgetmanager.model;

import jakarta.persistence.*;
 
@Entity
@Table(name = "uscite")
public class Withdrawal extends BaseModel {
 
    @Column(name = "value", nullable = false)
    private Long value;
 
    @Column(name = "label")
    private String label;
 
    public Withdrawal() {}
 
    public Withdrawal(Long value, String label) {
        this.value = value;
        this.label = label;
    }
 
    public Long getValue() { return value; }
    public void setValue(Long value) { this.value = value; }
 
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
}