package com.salessparrow.api.lib.crmActions.describeAccount;

import java.util.ArrayList;
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
import com.salessparrow.api.dto.entities.DescribeAccountFieldEntity;
import com.salessparrow.api.dto.formatter.DescribeAccountFormatterDto;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.Util;
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.globalConstants.SalesforceConstants;
import com.salessparrow.api.lib.httpLib.HttpClient;
import com.salessparrow.api.lib.salesforce.dto.CompositeRequestDto;
import com.salessparrow.api.lib.salesforce.dto.SalesforceDescribeAccountDto;
import com.salessparrow.api.lib.salesforce.dto.SalesforceErrorObject;
import com.salessparrow.api.lib.salesforce.helper.MakeCompositeRequest;
import com.salessparrow.api.lib.salesforce.helper.SalesforceCompositeResponseHelper;

/**
 * DescribeSalesforceAccount is a class for the DescribeAccount service for the Salesforce
 * CRM.
 **/
@Component
public class DescribeSalesforceAccount implements DescribeAccountInterface {

	@Autowired
	private SalesforceConstants salesforceConstants;

	@Autowired
	private MakeCompositeRequest makeCompositeRequest;

	@Autowired
	private SalesforceCompositeResponseHelper salesforceCompositeResponseHelper;

	Logger logger = LoggerFactory.getLogger(DescribeSalesforceAccount.class);

	/**
	 * Describe the accounts fields
	 * @param user
	 * @return DescribeAccountFormatterDto
	 **/
	public DescribeAccountFormatterDto describeAccount(User user) {
		String salesforceUserId = user.getExternalUserId();
		String url = salesforceConstants.describeAccountLayoutPath();

		CompositeRequestDto compositeReq = new CompositeRequestDto("GET", url, "describeAccount");

		List<CompositeRequestDto> compositeRequests = new ArrayList<CompositeRequestDto>();
		compositeRequests.add(compositeReq);

		HttpClient.HttpResponse response = makeCompositeRequest.makePostRequest(compositeRequests, salesforceUserId);

		return parseResponse(response.getResponseBody());
	}

	/**
	 * Parse Response
	 * @param responseBody
	 * @return DescribeAccountFormatterDto
	 **/
	public DescribeAccountFormatterDto parseResponse(String responseBody) {

		logger.info("Parsing response body");
		Util util = new Util();
		JsonNode rootNode = util.getJsonNode(responseBody);

		SalesforceErrorObject errorObject = salesforceCompositeResponseHelper
			.getErrorObjectFromCompositeResponse(rootNode);

		if (!errorObject.isSuccess()) {
			throw new CustomException(
					new ErrorObject("l_ca_da_dsa_pr_1", errorObject.getErrorCode(), errorObject.getMessage()));
		}

		JsonNode describeAccountResponseBody = rootNode.get("compositeResponse")
			.get(0)
			.get("body")
			.get("layouts")
			.get(0)
			.get("detailLayoutSections")
			.get(0)
			.get("layoutRows");

		Map<String, DescribeAccountFieldEntity> describeAccountFieldEntityMap = new HashMap<String, DescribeAccountFieldEntity>();
		for (JsonNode node : describeAccountResponseBody) {
			JsonNode layoutItems = node.get("layoutItems");
			JsonNode layoutItem = layoutItems.get(0);

			Boolean isRequired = layoutItem.get("required").asBoolean();
			if (!isRequired) {
				continue;
			}

			JsonNode details = layoutItem.get("layoutComponents").get(0).get("details");

			if (details != null) {
				ObjectMapper mapper = new ObjectMapper();
				mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
				SalesforceDescribeAccountDto salesforceDescribeAccountDto = mapper.convertValue(details,
						SalesforceDescribeAccountDto.class);

				DescribeAccountFieldEntity describeAccountFieldEntity = salesforceDescribeAccountDto
					.describeAccountFieldEntity();

				describeAccountFieldEntityMap.put(describeAccountFieldEntity.getName(), describeAccountFieldEntity);
			}
		}

		DescribeAccountFormatterDto describeAccountFormatterDto = new DescribeAccountFormatterDto();

		describeAccountFormatterDto.setFields(describeAccountFieldEntityMap);

		return describeAccountFormatterDto;
	}

}