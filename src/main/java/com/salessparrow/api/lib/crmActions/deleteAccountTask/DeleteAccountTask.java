package com.salessparrow.api.lib.crmActions.deleteAccountTask;

import org.springframework.stereotype.Component;

import com.salessparrow.api.domain.User;

@Component
public interface DeleteAccountTask {
    public void deleteAccountTask(User user, String accountId, String taskId);
}
