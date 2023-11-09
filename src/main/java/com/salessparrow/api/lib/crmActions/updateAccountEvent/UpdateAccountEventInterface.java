package com.salessparrow.api.lib.crmActions.updateAccountEvent;

import org.springframework.stereotype.Component;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.requestMapper.UpdateAccountEventDto;

/**
 * UpdateAccountEventInterface is an interface for the update event action for the CRM.
 */
@Component
public interface UpdateAccountEventInterface {

	public void updateEvent(User user, String accountId, String eventId, UpdateAccountEventDto updateEventDto);

}
