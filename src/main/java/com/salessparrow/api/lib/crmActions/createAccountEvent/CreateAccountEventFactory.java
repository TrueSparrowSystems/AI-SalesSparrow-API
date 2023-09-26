package com.salessparrow.api.lib.crmActions.createAccountEvent;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.formatter.CreateEventFormatterDto;
import com.salessparrow.api.dto.requestMapper.CreateAccountEventDto;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.globalConstants.UserConstants;

/**
 * CreateAccountEventFactory is a factory class for the create event action for the CRM.
 */
@Component
public class CreateAccountEventFactory {

	private Logger logger = org.slf4j.LoggerFactory.getLogger(CreateAccountEventFactory.class);

	@Autowired
	private CreateSalesforceAccountEvent createSalesforceEvent;

	/**
	 * Create an event for a given account
	 * @param user
	 * @param accountId
	 * @return CreateEventFormatterDto
	 **/
	public CreateEventFormatterDto createEvent(User user, String accountId, CreateAccountEventDto createEventDto) {
		logger.info("Create Event Factory started");

		switch (user.getUserKind()) {
			case UserConstants.SALESFORCE_USER_KIND:
				return createSalesforceEvent.createEvent(user, accountId, createEventDto);
			default:
				throw new CustomException(
						new ErrorObject("l_ca_cae_caef_cae_1", "something_went_wrong", "Invalid user kind."));
		}
	}

}
