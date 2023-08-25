package com.salessparrow.api.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpHeaders;

import com.salessparrow.api.services.salesforce.RedirectUrlService;
import com.salessparrow.api.dto.formatter.RedirectUrlFormatterDto;
import com.salessparrow.api.dto.formatter.SalesforceConnectFormatterDto;
import com.salessparrow.api.dto.requestMapper.SalesforceConnectDto;
import com.salessparrow.api.dto.requestMapper.SalesforceRedirectUrlDto;
import com.salessparrow.api.lib.CookieHelper;
import com.salessparrow.api.services.salesforce.AuthService;
import com.salessparrow.api.services.salesforce.AuthService.AuthServiceDto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@Validated
public class AuthController {

  Logger logger = LoggerFactory.getLogger(AuthController.class);

  @Autowired
  private RedirectUrlService redirectUrlService;

  @Autowired
  private AuthService authService;

  @Autowired
  private CookieHelper cookieHelper;

  @GetMapping("/salesforce/redirect-url")
  public ResponseEntity<RedirectUrlFormatterDto> getSalesforceRedirectUrl(
      @Valid @ModelAttribute SalesforceRedirectUrlDto salesforceRedirectUrlDto) {
    
    RedirectUrlFormatterDto redirectUrlFormatterDto = redirectUrlService.getSalesforceOauthUrl(salesforceRedirectUrlDto);
    
    return ResponseEntity.ok().body(redirectUrlFormatterDto);
  }

  @PostMapping("/salesforce/connect")
  public ResponseEntity<SalesforceConnectFormatterDto> connectToSalesforce(HttpServletRequest request,
      @Valid @RequestBody SalesforceConnectDto salesforceConnectDto) {
    logger.info("Salesforce connection request received");

    AuthServiceDto authServiceResponse = authService.connectToSalesforce(salesforceConnectDto, request);

    HttpHeaders headers = new HttpHeaders();
    headers = cookieHelper.setUserCookie(authServiceResponse.getCurrentUserLoginCookie(), headers);

    SalesforceConnectFormatterDto salesforceConnectResponse = new SalesforceConnectFormatterDto();
    salesforceConnectResponse.setCurrentUser(authServiceResponse.getCurrentUser());

    return ResponseEntity.ok().headers(headers).body(salesforceConnectResponse);
  }

  @PostMapping("/logout")
  public ResponseEntity<String> logout(HttpServletRequest request) {
    logger.info("User logout request received");

    HttpHeaders headers = new HttpHeaders();
    headers = cookieHelper.clearUserCookie(headers);

    return ResponseEntity.ok().headers(headers).body(null);
  }
}
