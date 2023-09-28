package com.salessparrow.api.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.salessparrow.api.dto.formatter.CreateTaskFormatterDto;
import com.salessparrow.api.dto.requestMapper.CreateAccountTaskDto;
import com.salessparrow.api.dto.requestMapper.UpdateAccountTaskDto;
import com.salessparrow.api.services.accountTask.CreateTaskService;
import com.salessparrow.api.services.accountTask.DeleteTaskService;
import com.salessparrow.api.dto.formatter.GetTasksListFormatterDto;
import com.salessparrow.api.services.accountTask.GetAccountTasksListService;
import com.salessparrow.api.services.accountTask.UpdateAccountTaskService;

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
	private DeleteTaskService deleteTaskService;

	@Autowired
	private GetAccountTasksListService getAccountTasksListService;

	@Autowired
	private UpdateAccountTaskService updateTaskService;

	@PostMapping("/{account_id}/tasks")
	public ResponseEntity<CreateTaskFormatterDto> createTask(HttpServletRequest request,
			@PathVariable("account_id") String accountId, @Valid @RequestBody CreateAccountTaskDto task) {
		logger.info("Create task request received");

		CreateTaskFormatterDto createTaskFormatterDto = createTaskService.createAccountTask(request, accountId, task);

		return ResponseEntity.status(HttpStatus.CREATED).body(createTaskFormatterDto);
	}

	@GetMapping("/{account_id}/tasks")
	public ResponseEntity<GetTasksListFormatterDto> getTasksList(HttpServletRequest request,
			@PathVariable("account_id") String accountId) {
		logger.info("Get tasks list request received");

		GetTasksListFormatterDto getTasksListFormatterDto = getAccountTasksListService.getAccountTasksList(request,
				accountId);
		return ResponseEntity.status(HttpStatus.OK).body(getTasksListFormatterDto);
	}

	@DeleteMapping("/{account_id}/tasks/{task_id}")
	public ResponseEntity<Void> deleteTask(HttpServletRequest request, @PathVariable("account_id") String accountId,
			@PathVariable("task_id") String taskId) {
		logger.info("Delete task request received");

		deleteTaskService.deleteAccountTask(request, accountId, taskId);

		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

	@PutMapping("/{task_id}")
	public ResponseEntity<Void> updateTask(HttpServletRequest request, @PathVariable("account_id") String accountId,
			@PathVariable("task_id") String taskId, @Valid @RequestBody UpdateAccountTaskDto updateTaskDto) {
		logger.info("Update task request received");

		updateTaskService.updateAccountTask(request, accountId, taskId, updateTaskDto);

		return ResponseEntity.status(HttpStatus.OK).build();
	}

}
