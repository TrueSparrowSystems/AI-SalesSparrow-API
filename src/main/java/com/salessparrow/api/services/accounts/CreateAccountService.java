package com.salessparrow.api.services.accounts;

import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.formatter.CreateAccountFormatterDto;
import com.salessparrow.api.lib.crmActions.createAccount.CreateAccountFactory;

import jakarta.servlet.http.HttpServletRequest;

/**
 * CreateAccountService is a service class for the create account action for the CRM.
 */
@Service
public class CreateAccountService {

	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(CreateAccountService.class);

	@Autowired
	private CreateAccountFactory createAccountFactory;

	/**
	 * Create an account for a specific account.
	 * @param request
	 * @param accountId
	 * @param createAccountDto
	 * @return CreateAccountFormatterDto
	 */
	public CreateAccountFormatterDto createAccount(HttpServletRequest request, Map<String, String> createAccountDto) {
		logger.info("Create Account Service started");

		User currentUser = (User) request.getAttribute("current_user");

		return createAccountFactory.createAccount(currentUser, createAccountDto);
	}

}