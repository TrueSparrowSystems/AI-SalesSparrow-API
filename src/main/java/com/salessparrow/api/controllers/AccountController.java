package com.salessparrow.api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.salessparrow.api.dto.NoteDto;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/accounts")
@Validated
public class AccountController {

  @PostMapping("/{account_id}/notes")
  public ResponseEntity<String> addNoteToAccount(
    @PathVariable("account_id") String accountId, 
    @Valid @RequestBody NoteDto note
  ) {
    return ResponseEntity.ok("Note added to Account");
  }
}
