package com.salessparrow.api.lib.crmActions.updateAccount;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
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
 * UpdateSalesforceAccount is a class that updates an account in Salesforce.
 */
@Component
public class UpdateSalesforceAccount implements UpdateAccountInterface {

	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(UpdateSalesforceAccount.class);

	@Autowired
	private SalesforceConstants salesforceConstants;

	@Autowired
	private MakeCompositeRequest makeCompositeRequest;

	@Autowired
	private SalesforceCompositeResponseHelper salesforceCompositeResponseHelper;

	/**
	 * Update an account for a given account id.
	 * @param user
	 * @param accountId
	 * @param updateAccountDto
	 * @return void
	 */
	public void updateAccount(User user, String accountId, Map<String, String> updateAccountDto) {
		logger.info("Update Salesforce Account started");

		String salesforceUserId = user.getExternalUserId();

		CompositeRequestDto updateAccountCompositeRequestDto = new CompositeRequestDto("PATCH",
				salesforceConstants.salesforceAccountByIdUrl(accountId), "UpdateAccount", updateAccountDto);

		List<CompositeRequestDto> compositeRequests = new ArrayList<CompositeRequestDto>();
		compositeRequests.add(updateAccountCompositeRequestDto);

		HttpClient.HttpResponse response = makeCompositeRequest.makePostRequest(compositeRequests, salesforceUserId);

		parseResponse(response.getResponseBody());
	}

	/**
	 * Parse the response from Salesforce.
	 * @param updateAccountResponse
	 * @return void
	 */
	private void parseResponse(String updateAccountResponse) {
		logger.info("Parsing the response from Salesforce");

		Util util = new Util();
		JsonNode rootNode = util.getJsonNode(updateAccountResponse);

		SalesforceErrorObject errorObject = salesforceCompositeResponseHelper
			.getErrorObjectFromCompositeResponse(rootNode);

		if (!errorObject.isSuccess()) {

			if (errorObject.getErrorCode().equals("invalid_params")) {
				throw new CustomException(new ParamErrorObject("l_ca_ca_csa_pr_1", errorObject.getMessage(),
						Arrays.asList("invalid_account_data")));
			}
			else {
				throw new CustomException(
						new ErrorObject("l_ua_ua_usa_pr_2", errorObject.getErrorCode(), errorObject.getMessage()));
			}
		}
	}

}
