package com.salessparrow.api.lib.crmActions.createAccount;

import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.formatter.CreateAccountFormatterDto;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.globalConstants.UserConstants;

/**
 * CreateAccountFactory is a factory class for the create account action for the CRM.
 */
@Component
public class CreateAccountFactory {

	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(CreateAccountFactory.class);

	@Autowired
	private CreateSalesforceAccount createSalesforceAccount;

	/**
	 * Create an account
	 * @param user
	 * @param accountId
	 * @return CreateAccountFormatterDto
	 **/
	public CreateAccountFormatterDto createAccount(User user, Map<String, String> createAccountDto) {
		logger.info("Create Account Factory started");

		switch (user.getUserKind()) {
			case UserConstants.SALESFORCE_USER_KIND:
				return createSalesforceAccount.createAccount(user, createAccountDto);
			default:
				throw new CustomException(
						new ErrorObject("l_ca_ca_caf_ca_1", "something_went_wrong", "Invalid user kind."));
		}
	}

}
