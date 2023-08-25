package com.salessparrow.api.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.salessparrow.api.dto.formatter.CreateTaskFormatterDto;
import com.salessparrow.api.dto.formatter.GetTasksListFormatterDto;
import com.salessparrow.api.dto.requestMapper.CreateTaskInAccountDto;
import com.salessparrow.api.services.accountTask.CreateTaskService;
import com.salessparrow.api.services.accountTask.GetAccountTasksListService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/v1/accounts")
@Validated
public class AccountTaskController {
  Logger logger = LoggerFactory.getLogger(AccountTaskController.class);

  @Autowired
  private CreateTaskService createTaskService;

  @Autowired
  private GetAccountTasksListService getAccountTasksListService;
    
  @PostMapping("/{account_id}/tasks")
  public ResponseEntity<CreateTaskFormatterDto> createTask(
    HttpServletRequest request,
    @PathVariable("account_id") String accountId,
    @Valid @RequestBody CreateTaskInAccountDto task
  ){
    logger.info("Create task request received");
    CreateTaskFormatterDto createTaskFormatterDto = createTaskService.createTaskInAccount(request, accountId, task);
    return ResponseEntity.status(HttpStatus.CREATED).body(createTaskFormatterDto);
  }

  @GetMapping("/{account_id}/tasks")
  public ResponseEntity<GetTasksListFormatterDto> getTasksList(
    HttpServletRequest request,
    @PathVariable("account_id") String accountId
  ){
    logger.info("Get tasks list request received");
  
    GetTasksListFormatterDto getTasksListFormatterDto = getAccountTasksListService.getAccountTasksList(request, accountId);
    return ResponseEntity.status(HttpStatus.OK).body(getTasksListFormatterDto);
  }
}
