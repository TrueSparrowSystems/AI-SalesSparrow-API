package com.salessparrow.api.services.accountTask;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.formatter.GetTaskDetailsFormatterDto;
import com.salessparrow.api.lib.crmActions.getAccountTaskDetails.GetAccountTaskDetailsFactory;

import jakarta.servlet.http.HttpServletRequest;

/**
 * GetAccountTaskDetailsService is a service class for the getTaskDetails action for the
 * CRM.
 */
@Service
public class GetAccountTaskDetailsService {

	@Autowired
	private GetAccountTaskDetailsFactory getTaskDetailsFactory;

	/**
	 * Get the details of a task
	 * @param accountId
	 * @param taskId
	 * @return GetTaskDetailsFormatterDto
	 */
	public GetTaskDetailsFormatterDto getTaskDetails(HttpServletRequest request, String taskId) {
		User currentUser = (User) request.getAttribute("current_user");

		return getTaskDetailsFactory.getTaskDetails(currentUser, taskId);
	}

}
