package com.salessparrow.api.lib.crmActions.updateAccountTask;

import org.springframework.stereotype.Component;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.requestMapper.UpdateAccountTaskDto;

/**
 * UpdateAccountTaskInterface is an interface for the update task action for the CRM.
 */
@Component
public interface UpdateAccountTaskInterface {

	public void updateTask(User user, String accountId, String taskId, UpdateAccountTaskDto updateTaskDto);

}
