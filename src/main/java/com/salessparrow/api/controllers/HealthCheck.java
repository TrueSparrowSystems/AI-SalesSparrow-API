package com.salessparrow.api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health-check")
public class HealthCheck {
  
  @GetMapping("")
  public ResponseEntity<String> HealthCheck() {
    return ResponseEntity.ok().body("OK");
  }
}
