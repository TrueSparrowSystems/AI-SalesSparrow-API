package com.salessparrow.api.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import com.salessparrow.api.dto.SalesforceRedirectUrlDto;

import com.salessparrow.api.services.salesforce.RedirectUrlService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth/")
@Validated
public class AuthController {

  Logger logger = LoggerFactory.getLogger(AuthController.class);

  @Autowired
  private RedirectUrlService redirectUrlService = new RedirectUrlService();

  @GetMapping("salesforce/redirect-url")
  public ResponseEntity<RedirectUrlService.RedirectUrlServiceDto> getSalesforceRedirectUrl(
    @Valid @ModelAttribute SalesforceRedirectUrlDto salesforceRedirectUrlDto
  ) {
    RedirectUrlService.RedirectUrlServiceDto salesforceOauthServiceDto = redirectUrlService.getSalesforceOauthUrl(salesforceRedirectUrlDto);

    return ResponseEntity.ok().body(salesforceOauthServiceDto);
  }
}
