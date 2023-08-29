package com.salessparrow.api.services.accountTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.formatter.GetTasksListFormatterDto;
import com.salessparrow.api.lib.crmActions.getAccountTasksList.GetAccountTasksListFactory;

import jakarta.servlet.http.HttpServletRequest;

/**
 * GetAccountTasksListService class is responsible for getting list of account tasks in CRM
 */
@Service
public class GetAccountTasksListService {
  Logger logger = LoggerFactory.getLogger(GetAccountTasksListService.class);

  @Autowired
  private GetAccountTasksListFactory getAccountTasksListFactory;
  
  /**
   * Get list of account tasks from CRM
   * 
   * @param request HttpServletRequest object
   * @param accountId CRM account id
   * 
   * @return GetTasksListFormatterDto object
   */
  public GetTasksListFormatterDto getAccountTasksList(HttpServletRequest request, String accountId) {
    logger.info("getAccountTasksList Service called");
    
    User currentUser = (User) request.getAttribute("current_user");
    return getAccountTasksListFactory.getAccountTasksList(currentUser, accountId);
  }
}
