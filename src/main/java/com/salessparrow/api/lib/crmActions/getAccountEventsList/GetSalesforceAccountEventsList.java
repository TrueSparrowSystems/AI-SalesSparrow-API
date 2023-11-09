package com.salessparrow.api.lib.crmActions.getAccountEventsList;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.salessparrow.api.dto.entities.EventEntity;
import com.salessparrow.api.dto.formatter.GetEventsListFormatterDto;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.Util;
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.errorLib.ParamErrorObject;
import com.salessparrow.api.lib.globalConstants.SalesforceConstants;
import com.salessparrow.api.lib.httpLib.HttpClient;
import com.salessparrow.api.lib.salesforce.dto.CompositeRequestDto;
import com.salessparrow.api.lib.salesforce.dto.SalesforceGetEventsListDto;
import com.salessparrow.api.lib.salesforce.helper.MakeCompositeRequest;
import com.salessparrow.api.lib.salesforce.helper.SalesforceQueryBuilder;

/**
 * GetSalesforceAccountEventsList is a class for the GetAccountEventsList service for the
 * Salesforce CRM.
 */
@Component
public class GetSalesforceAccountEventsList {

	Logger logger = LoggerFactory.getLogger(GetSalesforceAccountEventsList.class);

	@Autowired
	private SalesforceConstants salesforceConstants;

	@Autowired
	private MakeCompositeRequest makeCompositeRequest;

	/**
	 * Get the list of events for a given account in salesforce
	 * @param user
	 * @param accountId
	 * @return GetEventsListFormatterDto
	 **/
	public GetEventsListFormatterDto getAccountEventsList(User user, String accountId) {
		logger.info("Salesforce getAccountEventsList action called");

		String salesforceUserId = user.getExternalUserId();

		SalesforceQueryBuilder salesforceQuery = new SalesforceQueryBuilder();
		String query = salesforceQuery.getAccountEventsQuery(accountId);

		String url = salesforceConstants.queryUrlPath() + query;

		CompositeRequestDto compositeReq = new CompositeRequestDto("GET", url, "GetEventsList");

		List<CompositeRequestDto> compositeRequests = new ArrayList<CompositeRequestDto>();
		compositeRequests.add(compositeReq);

		HttpClient.HttpResponse response = makeCompositeRequest.makePostRequest(compositeRequests, salesforceUserId);

		return parseResponse(response.getResponseBody());
	}

	/**
	 * Parse Response
	 * @param responseBody
	 * @return GetEventsListFormatterDto
	 **/
	public GetEventsListFormatterDto parseResponse(String responseBody) {

		List<String> eventIds = new ArrayList<String>();
		Map<String, EventEntity> eventIdToEntityMap = new HashMap<>();

		Util util = new Util();
		JsonNode rootNode = util.getJsonNode(responseBody);

		JsonNode getEventsCompositeResponse = rootNode.get("compositeResponse").get(0);
		Integer getEventsStatusCode = getEventsCompositeResponse.get("httpStatusCode").asInt();

		if (getEventsStatusCode != 200 && getEventsStatusCode != 201) {
			String errorBody = getEventsCompositeResponse.get("body").asText();

			if (getEventsStatusCode == 400) {
				throw new CustomException(
						new ParamErrorObject("l_ca_gael_gsael_pr_1", errorBody, Arrays.asList("invalid_account_id")));
			}
			else {
				throw new CustomException(new ErrorObject("l_ca_gael_gsael_pr_2", "something_went_wrong", errorBody));
			}
		}

		JsonNode recordsNode = rootNode.get("compositeResponse").get(0).get("body").get("records");

		for (JsonNode recordNode : recordsNode) {
			ObjectMapper mapper = new ObjectMapper();
			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			SalesforceGetEventsListDto salesforceGetEventsListDto = mapper.convertValue(recordNode,
					SalesforceGetEventsListDto.class);
			EventEntity eventEntity = salesforceGetEventsListDto.eventEntity();

			eventIds.add(eventEntity.getId());
			eventIdToEntityMap.put(eventEntity.getId(), eventEntity);
		}

		GetEventsListFormatterDto getEventsListFormatterDto = new GetEventsListFormatterDto();
		getEventsListFormatterDto.setEventMapById(eventIdToEntityMap);
		getEventsListFormatterDto.setEventIds(eventIds);

		return getEventsListFormatterDto;
	}

}
