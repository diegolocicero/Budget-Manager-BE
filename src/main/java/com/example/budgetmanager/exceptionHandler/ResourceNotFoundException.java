package com.example.budgetmanager.exceptionHandler;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String resource, Long id) {
        super(resource + " con id " + id + " non trovato");
    }
}