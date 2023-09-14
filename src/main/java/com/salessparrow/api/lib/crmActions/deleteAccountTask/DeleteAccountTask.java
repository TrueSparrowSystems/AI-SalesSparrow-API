package com.salessparrow.api.lib.crmActions.deleteAccountTask;

import org.springframework.stereotype.Component;

import com.salessparrow.api.domain.User;

/**
 * DeleteAccountTask is an interface for deleting a task in an account.
 */
@Component
public interface DeleteAccountTask {

	public void deleteAccountTask(User user, String accountId, String taskId);

}
