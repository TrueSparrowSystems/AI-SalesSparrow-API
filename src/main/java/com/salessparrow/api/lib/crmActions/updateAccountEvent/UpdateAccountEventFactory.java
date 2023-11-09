package com.salessparrow.api.lib.crmActions.updateAccountEvent;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.requestMapper.UpdateAccountEventDto;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.globalConstants.UserConstants;

/**
 * UpdateAccountEventFactory is a factory class for the update event action for the CRM.
 */
@Component
public class UpdateAccountEventFactory {

	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(UpdateAccountEventFactory.class);

	@Autowired
	private UpdateSalesforceAccountEvent updateSalesforceEvent;

	/**
	 * Update an event for a given account
	 * @param user
	 * @param accountId
	 * @return void
	 **/
	public void updateEvent(User user, String accountId, String eventId, UpdateAccountEventDto updateEventDto) {
		logger.info("Update Event Factory started");

		switch (user.getUserKind()) {
			case UserConstants.SALESFORCE_USER_KIND:
				updateSalesforceEvent.updateEvent(user, accountId, eventId, updateEventDto);
				return;
			default:
				throw new CustomException(
						new ErrorObject("l_ua_uae_uaef_uae_1", "something_went_wrong", "Invalid user kind."));
		}
	}

}
