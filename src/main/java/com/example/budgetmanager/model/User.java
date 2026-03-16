package com.example.budgetmanager.model;

import jakarta.persistence.*;
 
@Entity
@Table(name = "utenti")
public class User extends BaseModel {
 
    @Column(name = "username", nullable = false, unique = true)
    private String username;
 
    @Column(name = "password", nullable = false)
    private String password;
 
    @Column(name = "email", nullable = false, unique = true)
    private String email;
 
    public User() {}
 
    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
 
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
 
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
 
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
 