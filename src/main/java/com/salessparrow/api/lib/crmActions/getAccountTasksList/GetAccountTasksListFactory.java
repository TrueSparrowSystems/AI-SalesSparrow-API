package com.salessparrow.api.lib.crmActions.getAccountTasksList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.formatter.GetTasksListFormatterDto;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.globalConstants.UserConstants;

/**
 * GetAccountTasksListFactory is a factory class for the GetAccountTasksList action for
 * the CRM.
 */
@Component
public class GetAccountTasksListFactory {

	Logger logger = LoggerFactory.getLogger(GetAccountTasksListFactory.class);

	@Autowired
	private GetSalesforceAccountTasksList getSalesforceAccountTasksList;

	/**
	 * Get the list of tasks for a given account.
	 * @param user
	 * @param accountId
	 * @return GetTasksListFormatterDto
	 **/
	public GetTasksListFormatterDto getAccountTasksList(User user, String accountId) {
		logger.info("factory for getAccountTasksList action");

		switch (user.getUserKind()) {
			case UserConstants.SALESFORCE_USER_KIND:
				return getSalesforceAccountTasksList.getAccountTasksList(user, accountId);
			default:
				throw new CustomException(
						new ErrorObject("l_ca_gatl_gatlf_gtl_1", "something_went_wrong", "Invalid user kind."));
		}
	}

}
