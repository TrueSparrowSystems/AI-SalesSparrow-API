package com.salessparrow.api.lib.crmActions.createEvent;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salessparrow.api.domain.SalesforceUser;
import com.salessparrow.api.dto.formatter.CreateEventFormatterDto;
import com.salessparrow.api.dto.requestMapper.CreateEventDto;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.globalConstants.UserConstants;

/**
 * CreateEventFactory is a factory class for the create event action for the CRM.
 */
@Component
public class CreateEventFactory {
  private Logger logger = org.slf4j.LoggerFactory.getLogger(CreateEventFactory.class);

  @Autowired
  private CreateSalesforceEvent createSalesforceEvent;

  /**
	 * Create an event for a given account
	 * @param user
	 * @param accountId
	 * 
	 * @return CreateEventFormatterDto
	 **/
	public CreateEventFormatterDto createEvent(SalesforceUser user, String accountId, CreateEventDto createEventDto) {
    logger.info("Create Event Factory started");

		switch(user.getUserKind()) {
			case UserConstants.SALESFORCE_USER_KIND:
				return createSalesforceEvent.createEvent(user, accountId, createEventDto);
			default:
				throw new CustomException(
					new ErrorObject(
						"l_ca_ce_cef_ce_1",
						"something_went_wrong",
						"Invalid user kind."));
		}
	}
}
