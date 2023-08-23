package com.salessparrow.api.lib.crmActions.createTaskInAccount;

import org.springframework.stereotype.Component;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.formatter.CreateTaskFormatterDto;
import com.salessparrow.api.dto.requestMapper.CreateTaskDto;

/**
 * CreateTask interface is interface for createTask for various CRM services
 */
@Component
public interface CreateTaskInAccount {
    public CreateTaskFormatterDto createTask(User User,String accountId, CreateTaskDto task);
}
