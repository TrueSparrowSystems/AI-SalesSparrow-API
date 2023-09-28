package com.salessparrow.api.lib.crmActions.getAccountEventDetails;

import org.springframework.stereotype.Component;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.formatter.GetEventDetailsFormatterDto;

/**
 * GetAccountEventDetails is an interface for the getEventDetails action for the CRM.
 */
@Component
public interface GetAccountEventDetails {

	public GetEventDetailsFormatterDto getEventDetails(User user, String eventId);

}
