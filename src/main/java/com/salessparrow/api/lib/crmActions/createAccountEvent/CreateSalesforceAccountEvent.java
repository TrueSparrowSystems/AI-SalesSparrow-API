package com.salessparrow.api.lib.crmActions.createAccountEvent;

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
import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.formatter.CreateEventFormatterDto;
import com.salessparrow.api.dto.requestMapper.CreateAccountEventDto;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.Util;
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.globalConstants.SalesforceConstants;
import com.salessparrow.api.lib.httpLib.HttpClient;
import com.salessparrow.api.lib.salesforce.dto.CompositeRequestDto;
import com.salessparrow.api.lib.salesforce.dto.SalesforceCreateEventDto;
import com.salessparrow.api.lib.salesforce.helper.MakeCompositeRequest;

/**
 * CreateSalesforceAccountEvent is a class that creates an event for an account in
 * Salesforce.
 */
@Component
public class CreateSalesforceAccountEvent implements CreateAccountEventInterface {

	private Logger logger = org.slf4j.LoggerFactory.getLogger(CreateSalesforceAccountEvent.class);

	@Autowired
	private SalesforceConstants salesforceConstants;

	@Autowired
	private MakeCompositeRequest makeCompositeRequest;

	/**
	 * Create an event for a given account.
	 * @param user
	 * @param accountId
	 * @param createEventDto
	 * @return CreateEventFormatterDto
	 */
	public CreateEventFormatterDto createEvent(User user, String accountId, CreateAccountEventDto createEventDto) {
		logger.info("Create Salesforce Event started");

		String salesforceUserId = user.getExternalUserId();

		Util util = new Util();
		String eventDescription = util.unEscapeSpecialCharactersForPlainText(createEventDto.getDescription());
		String eventSubject = util.getTrimmedString(eventDescription, salesforceConstants.salesforceSubjectLength());

		Map<String, String> createEventBody = new HashMap<String, String>();
		createEventBody.put("Subject", eventSubject);
		createEventBody.put("Description", eventDescription);
		createEventBody.put("WhatId", accountId);
		createEventBody.put("StartDateTime", createEventDto.getStartDatetime().toString());
		createEventBody.put("EndDateTime", createEventDto.getEndDatetime().toString());

		CompositeRequestDto createEventCompositeRequestDto = new CompositeRequestDto("POST",
				salesforceConstants.salesforceCreateEventUrl(), "CreateEvent", createEventBody);

		List<CompositeRequestDto> compositeRequests = new ArrayList<CompositeRequestDto>();
		compositeRequests.add(createEventCompositeRequestDto);

		HttpClient.HttpResponse response = makeCompositeRequest.makePostRequest(compositeRequests, salesforceUserId);

		return parseResponse(response.getResponseBody());
	}

	/**
	 * Parse the response from Salesforce.
	 * @param createEventResponse
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

			throw new CustomException(new ErrorObject("l_ca_cae_csae_pr_1", "internal_server_error", errorBody));
		}

		JsonNode createEventNodeResponseBody = rootNode.get("compositeResponse").get(0).get("body");

		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		SalesforceCreateEventDto salesforceCreateEventDto = mapper.convertValue(createEventNodeResponseBody,
				SalesforceCreateEventDto.class);

		CreateEventFormatterDto createEventFormatterDto = new CreateEventFormatterDto();
		createEventFormatterDto.setEventId(salesforceCreateEventDto.getId());

		return createEventFormatterDto;
	}

}
