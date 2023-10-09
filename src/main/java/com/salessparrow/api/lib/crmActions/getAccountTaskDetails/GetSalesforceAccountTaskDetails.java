package com.salessparrow.api.lib.crmActions.getAccountTaskDetails;

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
import com.salessparrow.api.dto.entities.TaskEntity;
import com.salessparrow.api.dto.formatter.GetTaskDetailsFormatterDto;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.Util;
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.errorLib.ParamErrorObject;
import com.salessparrow.api.lib.globalConstants.SalesforceConstants;
import com.salessparrow.api.lib.httpLib.HttpClient;
import com.salessparrow.api.lib.salesforce.dto.CompositeRequestDto;
import com.salessparrow.api.lib.salesforce.dto.SalesforceGetTasksListDto;
import com.salessparrow.api.lib.salesforce.helper.MakeCompositeRequest;
import com.salessparrow.api.lib.salesforce.helper.SalesforceQueryBuilder;

@Component
public class GetSalesforceAccountTaskDetails implements GetAccountTaskDetails {

	Logger logger = LoggerFactory.getLogger(GetSalesforceAccountTaskDetails.class);

	@Autowired
	private SalesforceConstants salesforceConstants;

	@Autowired
	private MakeCompositeRequest makeCompositeRequest;

	/**
	 * Get the details of an task.
	 * @param user
	 * @param taskId
	 * @return GetTaskDetailsFormatterDto
	 **/
	public GetTaskDetailsFormatterDto getTaskDetails(User user, String taskId) {

		logger.info("Salesforce getTaskDetails action called");

		String salesforceUserId = user.getExternalUserId();

		SalesforceQueryBuilder salesforceQuery = new SalesforceQueryBuilder();
		String query = salesforceQuery.getAccountTaskDetailsUrl(taskId);

		String url = salesforceConstants.queryUrlPath() + query;

		CompositeRequestDto compositeReq = new CompositeRequestDto("GET", url, "GetTaskDetails");

		List<CompositeRequestDto> compositeRequests = new ArrayList<CompositeRequestDto>();
		compositeRequests.add(compositeReq);

		HttpClient.HttpResponse response = makeCompositeRequest.makePostRequest(compositeRequests, salesforceUserId);

		return parseResponse(response.getResponseBody());

	}

	/**
	 * Parse Response
	 * @param responseBody
	 * @return GetTaskDetailsFormatterDto
	 **/
	public GetTaskDetailsFormatterDto parseResponse(String responseBody) {

		Util util = new Util();
		JsonNode rootNode = util.getJsonNode(responseBody);

		JsonNode compositeResponse = rootNode.get("compositeResponse").get(0);
		Integer statusCode = compositeResponse.get("httpStatusCode").asInt();

		if (statusCode != 200 && statusCode != 201) {
			String errorBody = compositeResponse.get("body").asText();

			if (statusCode == 400) {
				throw new CustomException(
						new ParamErrorObject("l_ca_gatd_gsatd_pr_1", errorBody, Arrays.asList("invalid_account_id")));
			}
			else {
				throw new CustomException(new ErrorObject("l_ca_gatd_gsatd_pr_2", "something_went_wrong", errorBody));
			}
		}

		JsonNode recordsNode = rootNode.get("compositeResponse").get(0).get("body").get("records");
		;

		TaskEntity taskEntity = new TaskEntity();
		for (JsonNode recordNode : recordsNode) {
			ObjectMapper mapper = new ObjectMapper();
			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			SalesforceGetTasksListDto salesforceGetTasksListDto = mapper.convertValue(recordNode,
					SalesforceGetTasksListDto.class);
			taskEntity = salesforceGetTasksListDto.taskEntity();
		}

		GetTaskDetailsFormatterDto getTaskDetailsFormatterDto = new GetTaskDetailsFormatterDto();
		getTaskDetailsFormatterDto.setTaskDetail(taskEntity);

		return getTaskDetailsFormatterDto;
	}

}
