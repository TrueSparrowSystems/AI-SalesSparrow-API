package com.salessparrow.api.services.accountEvents;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.requestMapper.UpdateAccountEventDto;
import com.salessparrow.api.lib.crmActions.updateAccountEvent.UpdateAccountEventFactory;

import jakarta.servlet.http.HttpServletRequest;

/**
 * UpdateAccountEventService is a service class that handles the update of an event in an
 * account.
 */
@Service
public class UpdateAccountEventService {

	Logger logger = LoggerFactory.getLogger(UpdateAccountEventService.class);

	@Autowired
	private UpdateAccountEventFactory updateAccountEventFactory;

	/**
	 * Updates an event in an account.
	 * @param request
	 * @param accountId
	 * @param eventId
	 * @return void
	 */
	public void updateAccountEvent(HttpServletRequest request, String accountId, String eventId,
			UpdateAccountEventDto updateEventDto) {
		logger.info("Update event in account service called");

		User currentUser = (User) request.getAttribute("current_user");

		updateAccountEventFactory.updateEvent(currentUser, accountId, eventId, updateEventDto);
	}

}
