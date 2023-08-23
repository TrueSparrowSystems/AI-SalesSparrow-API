package com.salessparrow.api.services.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salessparrow.api.domain.SalesforceUser;
import com.salessparrow.api.dto.formatter.CreateEventFormatterDto;
import com.salessparrow.api.dto.requestMapper.CreateEventDto;
import com.salessparrow.api.lib.crmActions.createEvent.CreateEventFactory;

import jakarta.servlet.http.HttpServletRequest;

/**
 * CreateEventService is a service class for the create event action for the CRM.
 */
@Service
public class CreateEventService {

  @Autowired
  private CreateEventFactory createEventFactory;

  /**
   * Create an event for a specific account.
   * 
   * @param request
   * @param accountId
   * @param createEventDto
   * 
   * @return CreateEventFormatterDto
   */
  public CreateEventFormatterDto createEvent(HttpServletRequest request, String accountId, CreateEventDto createEventDto) {

    SalesforceUser currentUser = (SalesforceUser) request.getAttribute("current_user");

    return createEventFactory.createEvent(currentUser, accountId, createEventDto);
  }
}
