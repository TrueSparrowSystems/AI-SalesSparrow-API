package com.salessparrow.api.lib.crmActions.updateAccount;

import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.globalConstants.UserConstants;

/**
 * UpdateAccountFactory is a factory class for the update account action for the CRM.
 */
@Component
public class UpdateAccountFactory {

	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(UpdateAccountFactory.class);

	@Autowired
	private UpdateSalesforceAccount updateSalesforceAccount;

	/**
	 * Update an account details for a given account
	 * @param user
	 * @param accountId
	 * @return void
	 **/
	public void updateAccount(User user, String accountId, Map<String, String> updateAccountDto) {
		logger.info("Update Account Factory started");

		switch (user.getUserKind()) {
			case UserConstants.SALESFORCE_USER_KIND:
				updateSalesforceAccount.updateAccount(user, accountId, updateAccountDto);
				return;
			default:
				throw new CustomException(
						new ErrorObject("l_ua_ua_uaf_ua_1", "something_went_wrong", "Invalid user kind."));
		}
	}

}
