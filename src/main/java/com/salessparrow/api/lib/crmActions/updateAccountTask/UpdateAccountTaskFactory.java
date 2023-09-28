package com.salessparrow.api.lib.crmActions.updateAccountTask;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.requestMapper.UpdateAccountTaskDto;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.globalConstants.UserConstants;

/**
 * UpdateAccountTaskFactory is a factory class for the update task action for the CRM.
 */
@Component
public class UpdateAccountTaskFactory {

	private Logger logger = org.slf4j.LoggerFactory.getLogger(UpdateAccountTaskFactory.class);

	@Autowired
	private UpdateSalesforceAccountTask updateSalesforceTask;

	/**
	 * Update a task for a given account
	 * @param user
	 * @param accountId
	 * @return void
	 **/
	public void updateTask(User user, String accountId, String taskId, UpdateAccountTaskDto updateTaskDto) {
		logger.info("Update Task Factory started");

		switch (user.getUserKind()) {
			case UserConstants.SALESFORCE_USER_KIND:
				updateSalesforceTask.updateTask(user, accountId, taskId, updateTaskDto);
				return;
			default:
				throw new CustomException(
						new ErrorObject("l_ua_uae_uaef_uae_1", "something_went_wrong", "Invalid user kind."));
		}
	}

}
