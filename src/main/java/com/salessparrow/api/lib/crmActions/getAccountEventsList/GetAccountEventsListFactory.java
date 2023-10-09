package com.salessparrow.api.lib.crmActions.getAccountEventsList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.formatter.GetEventsListFormatterDto;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.globalConstants.UserConstants;

/**
 * GetAccountEventsListFactory is a factory class for the GetAccountEventsList action for
 * the CRM.
 */
@Component
public class GetAccountEventsListFactory {

	Logger logger = LoggerFactory.getLogger(GetAccountEventsListFactory.class);

	@Autowired
	private GetSalesforceAccountEventsList getSalesforceAccountEventsList;

	/**
	 * Get the list of events for a given account.
	 * @param user
	 * @param accountId
	 * @return GetEventsListFormatterDto
	 **/
	public GetEventsListFormatterDto getAccountEventsList(User user, String accountId) {
		logger.info("factory for getAccountEventsList action");

		switch (user.getUserKind()) {
			case UserConstants.SALESFORCE_USER_KIND:
				return getSalesforceAccountEventsList.getAccountEventsList(user, accountId);
			default:
				throw new CustomException(
						new ErrorObject("l_ca_gatl_gatlf_gtl_1", "something_went_wrong", "Invalid user kind."));
		}
	}

}
