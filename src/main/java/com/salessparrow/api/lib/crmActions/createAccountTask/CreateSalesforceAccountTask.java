package com.salessparrow.api.lib.crmActions.createAccountTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.formatter.CreateTaskFormatterDto;
import com.salessparrow.api.dto.requestMapper.CreateAccountTaskDto;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.Util;
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.globalConstants.SalesforceConstants;
import com.salessparrow.api.lib.httpLib.HttpClient;
import com.salessparrow.api.lib.salesforce.dto.CompositeRequestDto;
import com.salessparrow.api.lib.salesforce.dto.SalesforceCreateTaskDto;
import com.salessparrow.api.lib.salesforce.helper.MakeCompositeRequest;

/**
 * CreateSalesforceTask class is responsible for creating a task in Salesforce
 */
@Component
public class CreateSalesforceAccountTask implements CreateAccountTask{

    Logger logger = LoggerFactory.getLogger(CreateSalesforceAccountTask.class);

    @Autowired
    private SalesforceConstants salesforceConstants;

    @Autowired
    private MakeCompositeRequest makeCompositeRequest;
    
    /**
     * Create a task in Salesforce
     * 
     * @param User User object
     * @param accountId Salesforce account id
     * @param task CreateTaskDto object
     * 
     * @return CreateTaskFormatterDto object
     */
    public CreateTaskFormatterDto createAccountTask(User User, String accountId, CreateAccountTaskDto task) {
        String salesforceUserId = User.getExternalUserId();

        String taskSubject = getTaskSubjectFromDescription(task);

        logger.info("performing create task in salesforce");

        Map<String, String> taskBody = new HashMap<String, String>();
        taskBody.put("Subject", taskSubject);
        taskBody.put("Description", task.getDescription());
        taskBody.put("OwnerId", task.getCrmOrganizationUserId());
        taskBody.put("ActivityDate", task.getDueDate());
        taskBody.put("WhatId", accountId);

        CompositeRequestDto createTaskCompositeRequestDto = new CompositeRequestDto(
            "POST",
            salesforceConstants.salesforceCreateTaskUrl(),
            "CreateTask",
            taskBody
        );

        List<CompositeRequestDto> compositeRequests = new ArrayList<CompositeRequestDto>();
        compositeRequests.add(createTaskCompositeRequestDto);

        HttpClient.HttpResponse response = makeCompositeRequest.makePostRequest(compositeRequests, salesforceUserId);

        return parseResponse(response.getResponseBody());
    }

    /**
     * Parse the response from Salesforce
     * 
     * @param createTaskResponse String response from Salesforce
     * 
     * @return CreateTaskFormatterDto object
     */
    private CreateTaskFormatterDto parseResponse(String createTaskResponse){
        Util util = new Util();
        JsonNode rootNode = util.getJsonNode(createTaskResponse);

        logger.info("parsing response from salesforce");

        JsonNode createTaskCompositeResponse = rootNode.get("compositeResponse").get(0);
        String createTaskStatusCode = createTaskCompositeResponse.get("httpStatusCode").asText();

        if (!createTaskStatusCode.equals("200") && !createTaskStatusCode.equals("201")) {
            String errorBody = createTaskCompositeResponse.get("body").asText();

            throw new CustomException(
            new ErrorObject(
                "l_ca_ct_cst_1",
                "internal_server_error",
                errorBody
            )); 
        }

        JsonNode createTaskNodeResponseBody = rootNode.get("compositeResponse").get(0).get("body");

        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        SalesforceCreateTaskDto salesforceCreateTaskDto = mapper.convertValue(createTaskNodeResponseBody, SalesforceCreateTaskDto.class);

        CreateTaskFormatterDto createTaskFormatterDto = new CreateTaskFormatterDto();
        createTaskFormatterDto.setTaskId(salesforceCreateTaskDto.getId());

        return createTaskFormatterDto;
    }

    /**
     * Get task subject from description
     * 
     * @param task CreateTaskDto object
     * 
     * @return String task subject
     */
    private String getTaskSubjectFromDescription(CreateAccountTaskDto task) {
        logger.info("getting task subject from description");
        if (task.getDescription().length() < 60) {
            return task.getDescription();
        }

        return task.getDescription().substring(0, 60);
    }
}
