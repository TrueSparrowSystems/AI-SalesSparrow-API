package com.salessparrow.api.lib.crmActions.createAccountTask;

import org.springframework.stereotype.Component;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.formatter.CreateTaskFormatterDto;
import com.salessparrow.api.dto.requestMapper.CreateAccountTaskDto;

/**
 * CreateTask interface is interface for createTask for various CRM services
 */
@Component
public interface CreateAccountTask {

	public CreateTaskFormatterDto createAccountTask(User User, String accountId, CreateAccountTaskDto task);

}
