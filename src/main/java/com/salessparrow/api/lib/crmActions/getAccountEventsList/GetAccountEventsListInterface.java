package com.salessparrow.api.lib.crmActions.getAccountEventsList;

import org.springframework.stereotype.Component;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.formatter.GetEventsListFormatterDto;

/**
 * GetAccountEventsListInterface is an interface for the GetAccountEventsList action for
 * the CRM.
 */
@Component
public interface GetAccountEventsListInterface {

	public GetEventsListFormatterDto getAccountEventsList(User user, String accountId);

}
