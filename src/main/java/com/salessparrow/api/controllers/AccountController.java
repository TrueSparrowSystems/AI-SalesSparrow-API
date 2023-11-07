package com.salessparrow.api.controllers;

import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.salessparrow.api.dto.formatter.CreateAccountFormatterDto;
import com.salessparrow.api.dto.formatter.DescribeAccountFormatterDto;
import com.salessparrow.api.dto.formatter.GetAccountDetailsFormatterDto;
import com.salessparrow.api.dto.requestMapper.GetAccountsDto;
import com.salessparrow.api.dto.requestMapper.GetAccountsFeedDto;
import com.salessparrow.api.dto.responseMapper.GetAccountListResponseDto;
import com.salessparrow.api.dto.responseMapper.GetAccountsFeedResponseDto;
import com.salessparrow.api.services.accounts.CreateAccountService;
import com.salessparrow.api.services.accounts.DescribeAccountService;
import com.salessparrow.api.services.accounts.GetAccountDetailsService;
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

	@Autowired
	private DescribeAccountService describeAccountService;

	@Autowired
	private CreateAccountService createAccountService;

	@Autowired
	private GetAccountDetailsService getAccountDetailsService;

	@GetMapping("")
	public ResponseEntity<GetAccountListResponseDto> getAccounts(HttpServletRequest request,
			@Valid @ModelAttribute GetAccountsDto getAccountsDto) {
		logger.info("Get Accounts Request received");

		GetAccountListResponseDto getAccountsResponse = getAccountListService.getAccounts(request, getAccountsDto);

		return ResponseEntity.ok().body(getAccountsResponse);
	}

	@PostMapping("")
	public ResponseEntity<CreateAccountFormatterDto> createAccount(HttpServletRequest request,
			@Valid @RequestBody Map<String, String> createAccountDto) {
		logger.info("Create Account Request received");

		CreateAccountFormatterDto createAccountFormatterDto = createAccountService.createAccount(request,
				createAccountDto);

		return ResponseEntity.ok().body(createAccountFormatterDto);

	}

	@GetMapping("/feed")
	public ResponseEntity<GetAccountsFeedResponseDto> getFeed(HttpServletRequest request,
			@Valid @ModelAttribute GetAccountsFeedDto getAccountsFeedDto) {
		logger.info("Get Account Feed Request received");

		GetAccountsFeedResponseDto getAccountsFeedResponse = getAccountsFeedService.getAccountsFeed(request,
				getAccountsFeedDto);

		return ResponseEntity.ok().body(getAccountsFeedResponse);
	}

	@GetMapping("/describe")
	public ResponseEntity<DescribeAccountFormatterDto> describeAccounts(HttpServletRequest request) {
		logger.info("Get Account Describe Request received");

		DescribeAccountFormatterDto describeAccountFormatterDto = describeAccountService.describeAccount(request);

		return ResponseEntity.ok().body(describeAccountFormatterDto);
	}

	@GetMapping("/{account_id}")
	public ResponseEntity<GetAccountDetailsFormatterDto> getAccountById(HttpServletRequest request,
			@PathVariable("account_id") String accountId) {
		logger.info("Get Account request received");

		GetAccountDetailsFormatterDto getAccountDetailsFormatterDto = getAccountDetailsService
			.getAccountDetails(request, accountId);

		return ResponseEntity.ok().body(getAccountDetailsFormatterDto);
	}

}
