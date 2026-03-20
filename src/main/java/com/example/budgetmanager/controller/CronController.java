package com.example.budgetmanager.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cron")
public class CronController {

    @GetMapping
    public ResponseEntity<String> cron() {
        return ResponseEntity.ok("ok");
    }
}