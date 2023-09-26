package com.salessparrow.api.services.accountEvents;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.formatter.CreateEventFormatterDto;
import com.salessparrow.api.dto.requestMapper.CreateAccountEventDto;
import com.salessparrow.api.lib.crmActions.createAccountEvent.CreateAccountEventFactory;

import jakarta.servlet.http.HttpServletRequest;

/**
 * CreateAccountEventService is a service class for the create account event action for
 * the CRM.
 */
@Service
public class CreateAccountEventService {

	private Logger logger = org.slf4j.LoggerFactory.getLogger(CreateAccountEventService.class);

	@Autowired
	private CreateAccountEventFactory createAccountEventFactory;

	/**
	 * Create an event for a specific account.
	 * @param request
	 * @param accountId
	 * @param createEventDto
	 * @return CreateEventFormatterDto
	 */
	public CreateEventFormatterDto createEvent(HttpServletRequest request, String accountId,
			CreateAccountEventDto createEventDto) {
		logger.info("Create Account Event Service started");

		User currentUser = (User) request.getAttribute("current_user");

		return createAccountEventFactory.createEvent(currentUser, accountId, createEventDto);
	}

}