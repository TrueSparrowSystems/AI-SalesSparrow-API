package com.salessparrow.api.lib.crmActions.getAccountEventDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.formatter.GetEventDetailsFormatterDto;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.globalConstants.UserConstants;

/**
 * GetAccountEventDetailsFactory is a factory class for the getEventDetails action for the
 * CRM.
 */
@Component
public class GetAccountEventDetailsFactory {

	@Autowired
	GetSalesforceAccountEventDetails getSalesforceEventDetails;

	/**
	 * getEventDetails is a method that returns the details of an event.
	 * @param user
	 * @param eventId
	 * @return GetEventDetailsFormatterDto
	 */
	public GetEventDetailsFormatterDto getEventDetails(User user, String eventId) {

		switch (user.getUserKind()) {
			case UserConstants.SALESFORCE_USER_KIND:
				return getSalesforceEventDetails.getEventDetails(user, eventId);
			default:
				throw new CustomException(
						new ErrorObject("l_ca_gaed_gaedf_gaed_1", "something_went_wrong", "Invalid user kind."));
		}
	}

}
