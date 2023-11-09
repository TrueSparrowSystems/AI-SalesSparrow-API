package com.salessparrow.api.services.accounts;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.lib.crmActions.updateAccount.UpdateAccountFactory;

import jakarta.servlet.http.HttpServletRequest;

/**
 * UpdateAccountService is a service class that handles the update of an account.
 */
@Service
public class UpdateAccountService {

	Logger logger = LoggerFactory.getLogger(UpdateAccountService.class);

	@Autowired
	private UpdateAccountFactory updateAccountFactory;

	/**
	 * Updates an account.
	 * @param request
	 * @param accountId
	 * @return void
	 */
	public void updateAccount(HttpServletRequest request, String accountId, Map<String, String> updateAccountDto) {
		logger.info("Update account service called");

		User currentUser = (User) request.getAttribute("current_user");

		updateAccountFactory.updateAccount(currentUser, accountId, updateAccountDto);
	}

}
