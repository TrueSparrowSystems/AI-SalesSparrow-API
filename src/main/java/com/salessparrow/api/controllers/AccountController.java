package com.salessparrow.api.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.salessparrow.api.dto.NoteDto;
import com.salessparrow.api.dto.formatter.GetAccountsFormatterDto;
import com.salessparrow.api.services.accounts.GetAccountListService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/accounts")
@Validated
public class AccountController {

  @Autowired
  private GetAccountListService getAccountListService;

  @PostMapping("/{account_id}/notes")
  public ResponseEntity<String> addNoteToAccount(
    @PathVariable("account_id") String accountId, 
    @Valid @RequestBody NoteDto note
  ) {
    return ResponseEntity.ok("Note added to Account");
  }

  @GetMapping("/")
  public GetAccountsFormatterDto getAccounts(HttpServletRequest request, @RequestParam String q) {

    return getAccountListService.getAccounts(request, q);
  }
}
