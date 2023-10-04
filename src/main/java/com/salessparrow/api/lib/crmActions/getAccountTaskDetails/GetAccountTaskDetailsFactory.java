package com.salessparrow.api.lib.crmActions.getAccountTaskDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.formatter.GetTaskDetailsFormatterDto;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.globalConstants.UserConstants;

/**
 * GetAccountTaskDetailsFactory is a factory class for the getTaskDetails action for the
 * CRM.
 */
@Component
public class GetAccountTaskDetailsFactory {

	@Autowired
	GetSalesforceAccountTaskDetails getSalesforceTaskDetails;

	/**
	 * getTaskDetails is a method that returns the details of a task.
	 * @param user
	 * @param taskId
	 * @return GetTaskDetailsFormatterDto
	 */
	public GetTaskDetailsFormatterDto getTaskDetails(User user, String taskId) {

		switch (user.getUserKind()) {
			case UserConstants.SALESFORCE_USER_KIND:
				return getSalesforceTaskDetails.getTaskDetails(user, taskId);
			default:
				throw new CustomException(
						new ErrorObject("l_ca_gatd_gatdf_gatd_1", "something_went_wrong", "Invalid user kind."));
		}
	}

}
