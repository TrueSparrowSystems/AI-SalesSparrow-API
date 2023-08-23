package com.salessparrow.api.lib.crmActions.createEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salessparrow.api.domain.SalesforceUser;
import com.salessparrow.api.dto.formatter.CreateEventFormatterDto;
import com.salessparrow.api.dto.requestMapper.CreateEventDto;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.Base64Helper;
import com.salessparrow.api.lib.Util;
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.globalConstants.SalesforceConstants;
import com.salessparrow.api.lib.httpLib.HttpClient;
import com.salessparrow.api.lib.salesforce.dto.CompositeRequestDto;
import com.salessparrow.api.lib.salesforce.dto.SalesforceCreateEventDto;
import com.salessparrow.api.lib.salesforce.helper.MakeCompositeRequest;

/**
 * CreateSalesforceEvent is a class that creates an event for an account in Salesforce.
 */
@Component
public class CreateSalesforceEvent implements CreateEventInterface {

  private Logger logger = org.slf4j.LoggerFactory.getLogger(CreateSalesforceEvent.class);

  @Autowired
  private SalesforceConstants salesforceConstants;

  @Autowired
  private MakeCompositeRequest makeCompositeRequest;

  @Autowired
  private Base64Helper base64Helper;

  /**
   * Create an event for a given account.
   * 
   * @param user
   * @param accountId
   * @param createEventDto
   * 
   * @return CreateEventFormatterDto
   */
  public CreateEventFormatterDto createEvent(SalesforceUser user, String accountId, CreateEventDto createEventDto) {
    logger.info("Create Salesforce Event started");

    String salesforceUserId = user.getExternalUserId();

    String eventSubject = getEventSubjectFromDescription(createEventDto);
    // String eventDescription = base64Helper.base64Encode(createEventDto.getDescription());

    Map<String, String> createEventBody = new HashMap<String, String>();
    createEventBody.put("Subject", eventSubject);
    createEventBody.put("Description", createEventDto.getDescription());
    createEventBody.put("WhatId", accountId);
    createEventBody.put("StartDateTime", createEventDto.getStartDatetime().toString());
    createEventBody.put("EndDateTime", createEventDto.getEndDatetime().toString());

    CompositeRequestDto createEventCompositeRequestDto = new CompositeRequestDto(
      "POST", 
      salesforceConstants.salesforceCreateEventUrl(), 
      "CreateEvent",
      createEventBody
    );

    List<CompositeRequestDto> compositeRequests = new ArrayList<CompositeRequestDto>();
    compositeRequests.add(createEventCompositeRequestDto);

    HttpClient.HttpResponse response = makeCompositeRequest.makePostRequest(compositeRequests, salesforceUserId);

    return parseResponse(response.getResponseBody());
  }

  /**
   * Parse the response from Salesforce.
   *
   * @param createEventResponse
   * 
   * @return CreateEventFormatterDto - formatted response
   */
  private CreateEventFormatterDto parseResponse(String createEventResponse) {
    logger.info("Parsing the response from Salesforce");

    Util util = new Util();
    JsonNode rootNode = util.getJsonNode(createEventResponse);

    JsonNode createEventCompositeResponse = rootNode.get("compositeResponse").get(0);
    Integer createEventStatusCode = createEventCompositeResponse.get("httpStatusCode").asInt();

    if (createEventStatusCode != 200 && createEventStatusCode != 201) {
      String errorBody = createEventCompositeResponse.get("body").asText();

      throw new CustomException(
      new ErrorObject(
        "l_ca_ce_cse_pr_1",
        "internal_server_error",
        errorBody)); 
    }

    JsonNode createEventNodeResponseBody = rootNode.get("compositeResponse").get(0).get("body");

    ObjectMapper mapper = new ObjectMapper();
    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    SalesforceCreateEventDto salesforceCreateEventDto = mapper.convertValue(createEventNodeResponseBody, SalesforceCreateEventDto.class);

    CreateEventFormatterDto createEventFormatterDto = new CreateEventFormatterDto();
    createEventFormatterDto.setEventId(salesforceCreateEventDto.getId());

    return createEventFormatterDto;
  }

  /**
   * Get the first 60 characters of the event description.
   * 
   * @param createEventDto
   * 
   * @return String
   */
  private String getEventSubjectFromDescription(CreateEventDto createEventDto) {
    if (createEventDto.getDescription().length() < 60) {
      return createEventDto.getDescription();
    }

    return createEventDto.getDescription().substring(0, 60);
  }
}
