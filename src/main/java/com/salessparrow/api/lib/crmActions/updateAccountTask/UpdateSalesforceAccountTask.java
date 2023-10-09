package com.salessparrow.api.lib.crmActions.updateAccountTask;

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
import com.salessparrow.api.dto.requestMapper.UpdateAccountTaskDto;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.Util;
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.errorLib.ParamErrorObject;
import com.salessparrow.api.lib.globalConstants.SalesforceConstants;
import com.salessparrow.api.lib.httpLib.HttpClient;
import com.salessparrow.api.lib.salesforce.dto.CompositeRequestDto;
import com.salessparrow.api.lib.salesforce.helper.MakeCompositeRequest;

/**
 * UpdateSalesforceAccountTask is a class that updates a task for an account in
 * Salesforce.
 */
@Component
public class UpdateSalesforceAccountTask implements UpdateAccountTaskInterface {

	private Logger logger = org.slf4j.LoggerFactory.getLogger(UpdateSalesforceAccountTask.class);

	@Autowired
	private SalesforceConstants salesforceConstants;

	@Autowired
	private MakeCompositeRequest makeCompositeRequest;

	/**
	 * Update a task for a given account.
	 * @param user
	 * @param accountId
	 * @param updateTaskDto
	 * @return void
	 */
	public void updateTask(User user, String accountId, String taskId, UpdateAccountTaskDto updateTaskDto) {
		logger.info("Update Salesforce Task started");

		String salesforceUserId = user.getExternalUserId();

		Util util = new Util();
		String taskDescription = util.unEscapeSpecialCharactersForPlainText(updateTaskDto.getDescription());
		String taskSubject = util.getTrimmedString(taskDescription, salesforceConstants.salesforceTaskSubjectLength());

		Map<String, String> updateTaskBody = new HashMap<String, String>();
		updateTaskBody.put("Subject", taskSubject);
		updateTaskBody.put("Description", taskDescription);
		updateTaskBody.put("OwnerId", updateTaskDto.getCrmOrganizationUserId());
		updateTaskBody.put("ActivityDate", updateTaskDto.getDueDate());

		CompositeRequestDto updateTaskCompositeRequestDto = new CompositeRequestDto("PATCH",
				salesforceConstants.salesforceUpdateTaskUrl(taskId), "UpdateTask", updateTaskBody);

		List<CompositeRequestDto> compositeRequests = new ArrayList<CompositeRequestDto>();
		compositeRequests.add(updateTaskCompositeRequestDto);

		HttpClient.HttpResponse response = makeCompositeRequest.makePostRequest(compositeRequests, salesforceUserId);

		parseResponse(response.getResponseBody());
	}

	/**
	 * Parse the response from Salesforce.
	 * @param updateTaskResponse
	 * @return void
	 */
	private void parseResponse(String updateTaskResponse) {
		logger.info("Parsing the response from Salesforce");

		Util util = new Util();
		JsonNode rootNode = util.getJsonNode(updateTaskResponse);

		JsonNode compositeResponse = rootNode.get("compositeResponse").get(0);
		Integer statusCode = compositeResponse.get("httpStatusCode").asInt();

		if (statusCode != 200 && statusCode != 201 && statusCode != 204) {
			String errorBody = compositeResponse.get("body").asText();

			// MALFORMED_ID or NOT_FOUND
			if (statusCode == 400 || statusCode == 404) {

				throw new CustomException(
						new ParamErrorObject("l_ua_uat_usat_pr_1", errorBody, Arrays.asList("invalid_task_id")));
			}
			else {
				throw new CustomException(new ErrorObject("l_ua_uat_usat_pr_2", "something_went_wrong", errorBody));
			}
		}
	}

}
