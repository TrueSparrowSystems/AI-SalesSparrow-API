package com.salessparrow.api.lib.crmActions.deleteAccountEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.salessparrow.api.domain.User;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.Util;
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.errorLib.ParamErrorObject;
import com.salessparrow.api.lib.globalConstants.SalesforceConstants;
import com.salessparrow.api.lib.httpLib.HttpClient;
import com.salessparrow.api.lib.salesforce.dto.CompositeRequestDto;
import com.salessparrow.api.lib.salesforce.dto.SalesforceErrorObject;
import com.salessparrow.api.lib.salesforce.helper.MakeCompositeRequest;
import com.salessparrow.api.lib.salesforce.helper.SalesforceCompositeResponseHelper;

/**
 * DeleteSalesforceAccountEvent is a class that handles the deleting of an event in an
 * account for salesforce.
 */
@Component
public class DeleteSalesforceAccountEvent implements DeleteAccountEventInterface {

	private final Util util = new Util();

	Logger logger = LoggerFactory.getLogger(DeleteSalesforceAccountEvent.class);

	@Autowired
	private SalesforceConstants salesforceConstants;

	@Autowired
	private MakeCompositeRequest makeCompositeRequest;

	@Autowired
	private SalesforceCompositeResponseHelper salesforceCompositeResponseHelper;

	/**
	 * Deletes an event in an account for salesforce.
	 * @param user
	 * @param accountId
	 * @param eventId
	 * @return void
	 */
	public void deleteAccountEvent(User user, String accountId, String eventId) {
		logger.info("Delete Salesforce Account Event called");

		String salesforceUserId = user.getExternalUserId();

		String url = salesforceConstants.salesforceDeleteAccountEventUrl(eventId);

		CompositeRequestDto compositeReq = new CompositeRequestDto("DELETE", url, "DeleteEvent");

		List<CompositeRequestDto> compositeRequests = new ArrayList<CompositeRequestDto>();
		compositeRequests.add(compositeReq);

		HttpClient.HttpResponse response = makeCompositeRequest.makePostRequest(compositeRequests, salesforceUserId);

		parseResponse(response.getResponseBody());

	}

	/**
	 * Parses the response from salesforce.
	 * @param responseBody
	 * @return void
	 */
	private void parseResponse(String responseBody) {
		logger.info("Parsing response body");
		JsonNode rootNode = util.getJsonNode(responseBody);

		SalesforceErrorObject errorObject = salesforceCompositeResponseHelper
			.getErrorObjectFromCompositeResponse(rootNode);

		if (!errorObject.isSuccess()) {

			if (errorObject.getErrorCode().equals("invalid_params")) {
				throw new CustomException(new ParamErrorObject("l_ca_dae_dsae_pr_1", errorObject.getErrorCode(),
						Arrays.asList("invalid_event_id")));
			}
			else {
				throw new CustomException(
						new ErrorObject("l_ca_dae_dsae_pr_2", errorObject.getErrorCode(), errorObject.getMessage()));
			}
		}
	}

}
