package com.salessparrow.api.lib.crmActions.getAccountTaskDetails;

import org.springframework.stereotype.Component;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.formatter.GetTaskDetailsFormatterDto;

/**
 * GetAccountTaskDetails is an interface for the getTaskDetails action for the CRM.
 */
@Component
public interface GetAccountTaskDetails {

	public GetTaskDetailsFormatterDto getTaskDetails(User user, String taskId);

}
