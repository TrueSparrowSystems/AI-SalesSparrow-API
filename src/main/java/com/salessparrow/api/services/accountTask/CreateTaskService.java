package com.salessparrow.api.services.accountTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salessparrow.api.controllers.AccountTaskController;
import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.formatter.CreateTaskFormatterDto;
import com.salessparrow.api.dto.requestMapper.CreateTaskDto;
import com.salessparrow.api.lib.crmActions.createTask.CreateTaskFactory;

import jakarta.servlet.http.HttpServletRequest;

/**
 * CreateTaskService class is responsible for creating a task in CRM
 */
@Service
public class CreateTaskService {
    Logger logger = LoggerFactory.getLogger(AccountTaskController.class);

    @Autowired
    private CreateTaskFactory createTaskFactory;
    
    /**
     * Create a task in CRM
     * 
     * @param request HttpServletRequest object
     * @param accountId CRM account id
     * @param task CreateTaskDto object
     * 
     * @return CreateTaskFormatterDto object
     */
    public CreateTaskFormatterDto createTask(HttpServletRequest request, String accountId, CreateTaskDto task) {
        logger.info("inside createTask Service");
        User currentUser = (User) request.getAttribute("current_user");
        return createTaskFactory.createTask(currentUser, accountId, task);
    }
}
