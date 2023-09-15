package com.salessparrow.api.lib.crmActions.deleteAccountEvent;

import org.springframework.stereotype.Component;

import com.salessparrow.api.domain.User;

/**
 * DeleteAccountEvent is an interface for deleting an event in an account.
 */
@Component
public interface DeleteAccountEvent {

	public void deleteAccountEvent(User user, String accountId, String eventId);

}
