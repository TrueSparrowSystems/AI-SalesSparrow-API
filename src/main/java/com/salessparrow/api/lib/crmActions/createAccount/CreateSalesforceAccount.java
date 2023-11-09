package com.salessparrow.api.lib.crmActions.createAccount;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.formatter.CreateAccountFormatterDto;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.Util;
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.errorLib.ParamErrorObject;
import com.salessparrow.api.lib.globalConstants.SalesforceConstants;
import com.salessparrow.api.lib.httpLib.HttpClient;
import com.salessparrow.api.lib.salesforce.dto.CompositeRequestDto;
import com.salessparrow.api.lib.salesforce.dto.SalesforceCreateAccountDto;
import com.salessparrow.api.lib.salesforce.dto.SalesforceErrorObject;
import com.salessparrow.api.lib.salesforce.helper.MakeCompositeRequest;
import com.salessparrow.api.lib.salesforce.helper.SalesforceCompositeResponseHelper;

/**
 * CreateSalesforceAccount is a class that creates an account in Salesforce.
 */
@Component
public class CreateSalesforceAccount implements CreateAccountInterface {

	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(CreateSalesforceAccount.class);

	@Autowired
	private SalesforceConstants salesforceConstants;

	@Autowired
	private MakeCompositeRequest makeCompositeRequest;

	@Autowired
	private SalesforceCompositeResponseHelper salesforceCompositeResponseHelper;

	/**
	 * Create an account in Salesforce.
	 * @param user
	 * @param accountId
	 * @param createAccountDto
	 * @return CreateAccountFormatterDto
	 */
	public CreateAccountFormatterDto createAccount(User user, Map<String, String> createAccountBody) {
		logger.info("Create Salesforce Account started");

		String salesforceUserId = user.getExternalUserId();

		CompositeRequestDto createAccountCompositeRequestDto = new CompositeRequestDto("POST",
				salesforceConstants.salesforceCreateAccountUrl(), "CreateAccount", createAccountBody);

		List<CompositeRequestDto> compositeRequests = new ArrayList<CompositeRequestDto>();
		compositeRequests.add(createAccountCompositeRequestDto);

		HttpClient.HttpResponse response = makeCompositeRequest.makePostRequest(compositeRequests, salesforceUserId);

		return parseResponse(response.getResponseBody());
	}

	/**
	 * Parse the response from Salesforce.
	 * @param createAccountResponse
	 * @return CreateAccountFormatterDto - formatted response
	 */
	private CreateAccountFormatterDto parseResponse(String createAccountResponse) {
		logger.info("Parsing the response from Salesforce");

		Util util = new Util();
		JsonNode rootNode = util.getJsonNode(createAccountResponse);

		SalesforceErrorObject errorObject = salesforceCompositeResponseHelper
			.getErrorObjectFromCompositeResponse(rootNode);

		if (!errorObject.isSuccess()) {

			if (errorObject.getErrorCode().equals("invalid_params")) {
				throw new CustomException(new ParamErrorObject("l_ca_ca_csa_pr_1", errorObject.getMessage(),
						Arrays.asList("invalid_account_data")));
			}
			else {
				throw new CustomException(
						new ErrorObject("l_ca_ca_csa_pr_2", errorObject.getErrorCode(), errorObject.getMessage()));
			}
		}

		JsonNode createAccountNodeResponseBody = rootNode.get("compositeResponse").get(0).get("body");

		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		SalesforceCreateAccountDto salesforceCreateAccountDto = mapper.convertValue(createAccountNodeResponseBody,
				SalesforceCreateAccountDto.class);

		CreateAccountFormatterDto createAccountFormatterDto = new CreateAccountFormatterDto();
		createAccountFormatterDto.setAccountId(salesforceCreateAccountDto.getId());

		return createAccountFormatterDto;
	}

}
