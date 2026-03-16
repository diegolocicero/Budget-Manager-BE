package com.example.budgetmanager.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "entrate")
public class Deposit extends BaseModel {
 
    @Column(name = "value", nullable = false)
    private Long value;
 
    @Column(name = "label")
    private String label;
 
    public Deposit() {}
 
    public Deposit(Long value, String label) {
        this.value = value;
        this.label = label;
    }
 
    public Long getValue() { return value; }
    public void setValue(Long value) { this.value = value; }
 
    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
}
 
