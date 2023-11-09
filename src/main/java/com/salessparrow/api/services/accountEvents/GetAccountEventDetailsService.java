package com.salessparrow.api.services.accountEvents;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.formatter.GetEventDetailsFormatterDto;
import com.salessparrow.api.lib.crmActions.getAccountEventDetails.GetAccountEventDetailsFactory;

import jakarta.servlet.http.HttpServletRequest;

/**
 * GetAccountEventDetailsService is a service class for the getEventDetails action for the
 * CRM.
 */
@Service
public class GetAccountEventDetailsService {

	@Autowired
	private GetAccountEventDetailsFactory getEventDetailsFactory;

	/**
	 * Get the details of an event
	 * @param accountId
	 * @param eventId
	 * @return GetEventDetailsFormatterDto
	 */
	public GetEventDetailsFormatterDto getEventDetails(HttpServletRequest request, String eventId) {
		User currentUser = (User) request.getAttribute("current_user");

		return getEventDetailsFactory.getEventDetails(currentUser, eventId);
	}

}
