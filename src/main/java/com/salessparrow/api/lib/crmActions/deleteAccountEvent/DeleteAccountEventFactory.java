package com.salessparrow.api.lib.crmActions.deleteAccountEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.globalConstants.UserConstants;

/**
 * DeleteAccountEventFactory is a factory class that handles the deleting of an event in
 * an account.
 */
@Component
public class DeleteAccountEventFactory {

	Logger logger = LoggerFactory.getLogger(DeleteAccountEventFactory.class);

	@Autowired
	DeleteSalesforceAccountEvent deleteSalesforceAccountEvent;

	/**
	 * Deletes a event in an account.
	 * @param user
	 * @param accountId
	 * @param eventId
	 * @return void
	 */
	public void deleteAccountEvent(User user, String accountId, String eventId) {
		logger.info("Delete Account Event Factory called");
		switch (user.getUserKind()) {
			case UserConstants.SALESFORCE_USER_KIND:
				deleteSalesforceAccountEvent.deleteAccountEvent(user, accountId, eventId);
				break;
			default:
				throw new CustomException(
						new ErrorObject("l_ca_dae_daef_1", "something_went_wrong", "Invalid user kind."));
		}
	}

}
