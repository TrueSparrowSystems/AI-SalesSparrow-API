package com.salessparrow.api.lib.crmActions.getAccountTasksList;

import org.springframework.stereotype.Component;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.formatter.GetTasksListFormatterDto;

/**
 * GetAccountTasksListInterface is an interface for the GetAccountTasksList action for the
 * CRM.
 */
@Component
public interface GetAccountTasksListInterface {

	public GetTasksListFormatterDto getTasksList(User user, String accountId);

}
