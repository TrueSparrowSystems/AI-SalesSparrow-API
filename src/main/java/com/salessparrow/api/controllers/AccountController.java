package com.salessparrow.api.controllers;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.salessparrow.api.dto.formatter.GetAccountsFormatterDto;
import com.salessparrow.api.dto.requestMapper.GetAccountsDto;
import com.salessparrow.api.services.accounts.GetAccountListService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/accounts")
@Validated
public class AccountController {

  private Logger logger = org.slf4j.LoggerFactory.getLogger(AccountController.class);

  @Autowired
  private GetAccountListService getAccountListService;

  @GetMapping("")
  public ResponseEntity<GetAccountsFormatterDto> getAccounts(
    HttpServletRequest request, 
    @Valid @ModelAttribute GetAccountsDto getAccountsDto) {
    logger.info("Request received");

    GetAccountsFormatterDto getAccountsResponse = getAccountListService.getAccounts(request, getAccountsDto);

    return ResponseEntity.ok().body(getAccountsResponse);
  }
}
