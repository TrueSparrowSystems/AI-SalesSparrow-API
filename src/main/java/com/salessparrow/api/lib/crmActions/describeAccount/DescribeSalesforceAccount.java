package com.salessparrow.api.lib.crmActions.describeAccount;

import java.util.ArrayList;
import java.util.List;

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
		String url = salesforceConstants.describeAccountPath();

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

		JsonNode describeAccountResponseBody = rootNode.get("compositeResponse").get(0).get("body").get("fields");
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		SalesforceDescribeAccountDto[] salesforceDescribeAccountDtos = mapper.convertValue(describeAccountResponseBody,
				SalesforceDescribeAccountDto[].class);

		List<DescribeAccountFieldEntity> describeAccountFieldEntities = new ArrayList<DescribeAccountFieldEntity>();

		for (int i = 0; i < salesforceDescribeAccountDtos.length; i++) {
			SalesforceDescribeAccountDto salesforceDescribeAccountDto = salesforceDescribeAccountDtos[i];

			if (!salesforceDescribeAccountDto.getNillable() && salesforceDescribeAccountDto.getCreateable()
					&& !salesforceDescribeAccountDto.getName().equals("OwnerId")) {
				DescribeAccountFieldEntity describeAccountFieldEntity = salesforceDescribeAccountDto
					.describeAccountFieldEntity();

				describeAccountFieldEntities.add(describeAccountFieldEntity);
			}
		}

		DescribeAccountFormatterDto describeAccountFormatterDto = new DescribeAccountFormatterDto();
		describeAccountFormatterDto.setFields(describeAccountFieldEntities
			.toArray(new DescribeAccountFieldEntity[describeAccountFieldEntities.size()]));

		return describeAccountFormatterDto;
	}

}