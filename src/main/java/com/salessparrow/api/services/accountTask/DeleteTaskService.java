package com.salessparrow.api.services.accountTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.lib.crmActions.deleteAccountTask.DeleteAccountTaskFactory;

import jakarta.servlet.http.HttpServletRequest;

/**
 * DeleteTaskService is a service class that handles the deleting a task in an account.
 */
@Service
public class DeleteTaskService {

	Logger logger = LoggerFactory.getLogger(DeleteTaskService.class);

	@Autowired
	private DeleteAccountTaskFactory deleteAccountTaskFactory;

	/**
	 * Deletes a task in an account.
	 * @param request
	 * @param accountId
	 * @param taskId
	 * @return void
	 */
	public void deleteAccountTask(HttpServletRequest request, String accountId, String taskId) {
		logger.info("Delete task in account service called");

		User currentUser = (User) request.getAttribute("current_user");

		deleteAccountTaskFactory.deleteAccountTask(currentUser, accountId, taskId);
	}

}
