package com.salessparrow.api.services.accountTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.requestMapper.UpdateAccountTaskDto;
import com.salessparrow.api.lib.crmActions.updateAccountTask.UpdateAccountTaskFactory;

import jakarta.servlet.http.HttpServletRequest;

/**
 * UpdateAccountTaskService is a service class that handles the update of a task in an
 * account.
 */
@Service
public class UpdateAccountTaskService {

	Logger logger = LoggerFactory.getLogger(UpdateAccountTaskService.class);

	@Autowired
	private UpdateAccountTaskFactory updateAccountTaskFactory;

	/**
	 * Updates a task in an account.
	 * @param request
	 * @param accountId
	 * @param taskId
	 * @return void
	 */
	public void updateAccountTask(HttpServletRequest request, String accountId, String taskId,
			UpdateAccountTaskDto updateTaskDto) {
		logger.info("Update task in account service called");

		User currentUser = (User) request.getAttribute("current_user");

		updateAccountTaskFactory.updateTask(currentUser, accountId, taskId, updateTaskDto);
	}

}
