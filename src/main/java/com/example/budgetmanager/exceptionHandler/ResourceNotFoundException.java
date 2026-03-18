package com.example.budgetmanager.exceptionHandler;

import java.util.UUID;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resource, UUID id) {
        super(resource + " con id " + id + " non trovato");
    }

    public ResourceNotFoundException(String resource, Long id) {
        super(resource + " con id " + id + " non trovato");
    }
}