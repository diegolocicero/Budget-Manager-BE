package com.example.budgetmanager.dto;

import java.time.OffsetDateTime;

public class TransactionDTO {

    public enum Type {
        DEPOSIT, WITHDRAWAL
    }

    public static class Request {
        private Long value;
        private String label;

        public Request() {}

        public Long getValue() { return value; }
        public void setValue(Long value) { this.value = value; }

        public String getLabel() { return label; }
        public void setLabel(String label) { this.label = label; }
    }

    public static class Response {
        private Long id;
        private Long value;
        private String label;
        private OffsetDateTime createdAt;
        private Type type;

        public Response() {}

        public Response(Long id, Long value, String label, OffsetDateTime createdAt, Type type) {
            this.id = id;
            this.value = value;
            this.label = label;
            this.createdAt = createdAt;
            this.type = type;
        }

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public Long getValue() { return value; }
        public void setValue(Long value) { this.value = value; }

        public String getLabel() { return label; }
        public void setLabel(String label) { this.label = label; }

        public OffsetDateTime getCreatedAt() { return createdAt; }
        public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }

        public Type getType() { return type; }
        public void setType(Type type) { this.type = type; }
    }
}