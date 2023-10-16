package com.salessparrow.api.lib.crmActions.getAccountEventDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.entities.EventEntity;
import com.salessparrow.api.dto.formatter.GetEventDetailsFormatterDto;
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

@Component
public class GetSalesforceAccountEventDetails implements GetAccountEventDetails {

	Logger logger = LoggerFactory.getLogger(GetSalesforceAccountEventDetails.class);

	@Autowired
	private SalesforceConstants salesforceConstants;

	@Autowired
	private MakeCompositeRequest makeCompositeRequest;

	/**
	 * Get the details of an event.
	 * @param user
	 * @param eventId
	 * @return GetEventDetailsFormatterDto
	 **/
	public GetEventDetailsFormatterDto getEventDetails(User user, String eventId) {

		logger.info("Salesforce getEventDetails action called");

		String salesforceUserId = user.getExternalUserId();

		SalesforceQueryBuilder salesforceQuery = new SalesforceQueryBuilder();
		String query = salesforceQuery.getAccountEventDetailsUrl(eventId);

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
	public GetEventDetailsFormatterDto parseResponse(String responseBody) {

		Util util = new Util();
		JsonNode rootNode = util.getJsonNode(responseBody);

		JsonNode compositeResponse = rootNode.get("compositeResponse").get(0);
		Integer statusCode = compositeResponse.get("httpStatusCode").asInt();

		if (statusCode != 200 && statusCode != 201) {
			String errorBody = compositeResponse.get("body").asText();

			if (statusCode == 400) {
				throw new CustomException(
						new ParamErrorObject("l_ca_gaed_gsaed_pr_1", errorBody, Arrays.asList("invalid_account_id")));
			}
			else {
				throw new CustomException(new ErrorObject("l_ca_gaed_gsaed_pr_2", "something_went_wrong", errorBody));
			}
		}

		JsonNode recordsNode = rootNode.get("compositeResponse").get(0).get("body").get("records");

		EventEntity eventEntity = new EventEntity();
		if (recordsNode.size() > 0) {
			for (JsonNode recordNode : recordsNode) {
				ObjectMapper mapper = new ObjectMapper();
				mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
				SalesforceGetEventsListDto salesforceGetEventsListDto = mapper.convertValue(recordNode,
						SalesforceGetEventsListDto.class);
				eventEntity = salesforceGetEventsListDto.eventEntity();
			}
		}

		GetEventDetailsFormatterDto getEventDetailsFormatterDto = new GetEventDetailsFormatterDto();
		getEventDetailsFormatterDto.setEventDetail(eventEntity);

		return getEventDetailsFormatterDto;
	}

}
