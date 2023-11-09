package com.salessparrow.api.controllers;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.salessparrow.api.dto.requestMapper.GetAccountsDto;
import com.salessparrow.api.dto.requestMapper.GetAccountsFeedDto;
import com.salessparrow.api.dto.responseMapper.GetAccountListResponseDto;
import com.salessparrow.api.dto.responseMapper.GetAccountsFeedResponseDto;
import com.salessparrow.api.services.accounts.GetAccountListService;
import com.salessparrow.api.services.accounts.GetAccountsFeedService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/accounts")
@Validated
public class AccountController {

	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(AccountController.class);

	@Autowired
	private GetAccountListService getAccountListService;

	@Autowired
	private GetAccountsFeedService getAccountsFeedService;

	@GetMapping("")
	public ResponseEntity<GetAccountListResponseDto> getAccounts(HttpServletRequest request,
			@Valid @ModelAttribute GetAccountsDto getAccountsDto) {
		logger.info("Get Accounts Request received");

		GetAccountListResponseDto getAccountsResponse = getAccountListService.getAccounts(request, getAccountsDto);

		return ResponseEntity.ok().body(getAccountsResponse);
	}

	@GetMapping("/feed")
	public ResponseEntity<GetAccountsFeedResponseDto> getFeed(HttpServletRequest request,
			@Valid @ModelAttribute GetAccountsFeedDto getAccountsFeedDto) {
		logger.info("Get Account Feed Request received");

		GetAccountsFeedResponseDto getAccountsFeedResponse = getAccountsFeedService.getAccountsFeed(request,
				getAccountsFeedDto);

		return ResponseEntity.ok().body(getAccountsFeedResponse);
	}

}
