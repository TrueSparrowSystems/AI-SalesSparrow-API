package com.salessparrow.api.lib.crmActions.deleteAccountTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.globalConstants.UserConstants;

/**
 * DeleteAccountTaskFactory is a factory class that handles the deleting a task in an
 * account.
 */
@Component
public class DeleteAccountTaskFactory {

	Logger logger = LoggerFactory.getLogger(DeleteAccountTaskFactory.class);

	@Autowired
	DeleteSalesforceAccountTask deleteSalesforceAccountTask;

	/**
	 * Deletes a task in an account.
	 * @param user
	 * @param accountId
	 * @param taskId
	 * @return void
	 */
	public void deleteAccountTask(User user, String accountId, String taskId) {
		logger.info("Delete Account Task Factory called");
		switch (user.getUserKind()) {
			case UserConstants.SALESFORCE_USER_KIND:
				deleteSalesforceAccountTask.deleteAccountTask(user, accountId, taskId);
				break;
			default:
				throw new CustomException(
						new ErrorObject("l_ca_dat_datf_1", "something_went_wrong", "Invalid user kind."));
		}
	}

}
