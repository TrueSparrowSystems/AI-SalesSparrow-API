package com.salessparrow.api.services.accountEvents;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.lib.crmActions.deleteAccountEvent.DeleteAccountEventFactory;

import jakarta.servlet.http.HttpServletRequest;

/**
 * DeleteAccountEventService is a service class that handles the deleting of an event in
 * an account.
 */
@Service
public class DeleteAccountEventService {

	Logger logger = LoggerFactory.getLogger(DeleteAccountEventService.class);

	@Autowired
	private DeleteAccountEventFactory deleteAccountEventFactory;

	/**
	 * Deletes an event in an account.
	 * @param request
	 * @param accountId
	 * @param eventId
	 * @return void
	 */
	public void deleteAccountEvent(HttpServletRequest request, String accountId, String eventId) {
		logger.info("Delete event in account service called");

		User currentUser = (User) request.getAttribute("current_user");

		deleteAccountEventFactory.deleteAccountEvent(currentUser, accountId, eventId);
	}

}
