package com.salessparrow.api.lib.crmActions.createTaskInAccount;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.formatter.CreateTaskFormatterDto;
import com.salessparrow.api.dto.requestMapper.CreateTaskInAccountDto;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.globalConstants.UserConstants;

/**
 * CreateTaskFactory class is responsible for creating a task in CRM
 */
@Component
public class CreateTaskInAccountFactory {

    Logger logger = LoggerFactory.getLogger(CreateTaskInAccountFactory.class);

    @Autowired
    CreateSalesforceTaskInAccount createSalesforceTaskInAccount;

    /**
     * Create a task in CRM
     * 
     * @param user User object
     * @param accountId CRM account id
     * @param task CreateTaskDto object
     * 
     * @return CreateTaskFormatterDto object
     */
    public CreateTaskFormatterDto createTask(User user, String accountId, CreateTaskInAccountDto task) {
        switch(user.getUserKind()) {
        case UserConstants.SALESFORCE_USER_KIND:
          logger.info("calling createTask of salesforceCreateTask");
          return createSalesforceTaskInAccount.createTask(user, accountId, task);
        default:
          throw new CustomException(
            new ErrorObject(
              "l_ca_ct_ctf_1",
              "something_went_wrong",
              "Invalid user kind."));
        }
    }
}
