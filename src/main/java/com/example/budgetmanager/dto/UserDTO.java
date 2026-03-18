package com.example.budgetmanager.dto;

import java.time.OffsetDateTime;
import java.util.UUID;
 
public class UserDTO {
 
    public static class Request {
        private String username;
        private String password;
        private String email;
 
        public Request() {}
 
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
 
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
 
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }
 
    public static class Response {
        private UUID id;
        private String username;
        private String email;
        private OffsetDateTime createdAt;
 
        public Response() {}
 
        public Response(UUID id, String username, String email, OffsetDateTime createdAt) {
            this.id = id;
            this.username = username;
            this.email = email;
            this.createdAt = createdAt;
        }
 
        public UUID getId() { return id; }
        public void setId(UUID id) { this.id = id; }
 
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
 
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
 
        public OffsetDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }
    }
}