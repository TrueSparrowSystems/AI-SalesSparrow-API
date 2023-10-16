package com.salessparrow.api.lib.crmActions.updateAccountEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.requestMapper.UpdateAccountEventDto;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.Util;
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.errorLib.ParamErrorObject;
import com.salessparrow.api.lib.globalConstants.SalesforceConstants;
import com.salessparrow.api.lib.httpLib.HttpClient;
import com.salessparrow.api.lib.salesforce.dto.CompositeRequestDto;
import com.salessparrow.api.lib.salesforce.helper.MakeCompositeRequest;

/**
 * UpdateSalesforceAccountEvent is a class that updates an event for an account in
 * Salesforce.
 */
@Component
public class UpdateSalesforceAccountEvent implements UpdateAccountEventInterface {

	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(UpdateSalesforceAccountEvent.class);

	@Autowired
	private SalesforceConstants salesforceConstants;

	@Autowired
	private MakeCompositeRequest makeCompositeRequest;

	/**
	 * Update an event for a given account.
	 * @param user
	 * @param accountId
	 * @param updateEventDto
	 * @return void
	 */
	public void updateEvent(User user, String accountId, String eventId, UpdateAccountEventDto updateEventDto) {
		logger.info("Update Salesforce Event started");

		String salesforceUserId = user.getExternalUserId();

		String eventDescription = Util.unEscapeSpecialCharactersForPlainText(updateEventDto.getDescription());
		String eventSubject = Util.getTrimmedString(eventDescription,
				salesforceConstants.salesforceEventSubjectLength());

		Map<String, String> updateEventBody = new HashMap<String, String>();
		updateEventBody.put("Subject", eventSubject);
		updateEventBody.put("Description", eventDescription);
		updateEventBody.put("StartDateTime", updateEventDto.getStartDatetime().toString());
		updateEventBody.put("EndDateTime", updateEventDto.getEndDatetime().toString());

		CompositeRequestDto updateEventCompositeRequestDto = new CompositeRequestDto("PATCH",
				salesforceConstants.salesforceUpdateEventUrl(eventId), "UpdateEvent", updateEventBody);

		List<CompositeRequestDto> compositeRequests = new ArrayList<CompositeRequestDto>();
		compositeRequests.add(updateEventCompositeRequestDto);

		HttpClient.HttpResponse response = makeCompositeRequest.makePostRequest(compositeRequests, salesforceUserId);

		parseResponse(response.getResponseBody());
	}

	/**
	 * Parse the response from Salesforce.
	 * @param updateEventResponse
	 * @return void
	 */
	private void parseResponse(String updateEventResponse) {
		logger.info("Parsing the response from Salesforce");

		Util util = new Util();
		JsonNode rootNode = util.getJsonNode(updateEventResponse);

		JsonNode updateEventCompositeResponse = rootNode.get("compositeResponse").get(0);
		Integer updateEventStatusCode = updateEventCompositeResponse.get("httpStatusCode").asInt();

		if (updateEventStatusCode != 200 && updateEventStatusCode != 201 && updateEventStatusCode != 204) {
			String errorBody = updateEventCompositeResponse.get("body").asText();

			// MALFORMED_ID or NOT_FOUND
			if (updateEventStatusCode == 400 || updateEventStatusCode == 404) {

				throw new CustomException(
						new ParamErrorObject("l_ua_uae_usae_pr_1", errorBody, Arrays.asList("invalid_event_id")));
			}
			else {
				throw new CustomException(new ErrorObject("l_ua_uae_usae_pr_2", "something_went_wrong", errorBody));
			}
		}
	}

}
