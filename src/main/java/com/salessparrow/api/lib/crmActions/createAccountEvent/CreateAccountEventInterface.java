package com.salessparrow.api.lib.crmActions.createAccountEvent;

import org.springframework.stereotype.Component;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.formatter.CreateEventFormatterDto;
import com.salessparrow.api.dto.requestMapper.CreateAccountEventDto;

/**
 * CreateAccountEventInterface is an interface for the create event action for the CRM.
 */
@Component
public interface CreateAccountEventInterface {

	public CreateEventFormatterDto createEvent(User user, String accountId, CreateAccountEventDto createEventDto);

}
