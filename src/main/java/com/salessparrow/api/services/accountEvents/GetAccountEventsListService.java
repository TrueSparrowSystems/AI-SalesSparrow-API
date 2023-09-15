package com.salessparrow.api.services.accountEvents;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.formatter.GetEventsListFormatterDto;
import com.salessparrow.api.lib.crmActions.getAccountEventsList.GetAccountEventsListFactory;

import jakarta.servlet.http.HttpServletRequest;

/**
 * GetAccountEventsListService class is responsible for getting list of account events in
 * CRM
 */
@Service
public class GetAccountEventsListService {

	Logger logger = LoggerFactory.getLogger(GetAccountEventsListService.class);

	@Autowired
	private GetAccountEventsListFactory getAccountEventsListFactory;

	/**
	 * Get list of account events from CRM
	 * @param request HttpServletRequest object
	 * @param accountId CRM account id
	 * @return GetEventsListFormatterDto object
	 */
	public GetEventsListFormatterDto getAccountEventsList(HttpServletRequest request, String accountId) {
		logger.info("getAccountEventsList Service called");

		User currentUser = (User) request.getAttribute("current_user");
		return getAccountEventsListFactory.getAccountEventsList(currentUser, accountId);
	}

}
