package com.salessparrow.api.lib.crmActions.createEvent;

import org.springframework.stereotype.Component;

import com.salessparrow.api.domain.SalesforceUser;
import com.salessparrow.api.dto.formatter.CreateEventFormatterDto;
import com.salessparrow.api.dto.requestMapper.CreateEventDto;

/**
 * CreateEventInterface is an interface for the create event action for the CRM.
 */
@Component
public interface CreateEventInterface {
  public CreateEventFormatterDto createEvent(SalesforceUser user, String accountId, CreateEventDto createEventDto);
}
