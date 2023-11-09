package com.salessparrow.api.lib.crmActions.getAccountDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.entities.AccountContactAssociationsEntity;
import com.salessparrow.api.dto.entities.AccountEntity;
import com.salessparrow.api.dto.entities.ContactEntity;
import com.salessparrow.api.dto.formatter.GetAccountDetailsFormatterDto;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.Util;
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.errorLib.ParamErrorObject;
import com.salessparrow.api.lib.globalConstants.SalesforceConstants;
import com.salessparrow.api.lib.httpLib.HttpClient;
import com.salessparrow.api.lib.salesforce.dto.CompositeRequestDto;
import com.salessparrow.api.lib.salesforce.dto.SalesforceDescribeAccountDto;
import com.salessparrow.api.lib.salesforce.helper.MakeCompositeRequest;
import com.salessparrow.api.lib.salesforce.helper.SalesforceQueryBuilder;

@Component
public class GetSalesforceAccountDetails implements GetAccountDetails {

	Logger logger = LoggerFactory.getLogger(GetSalesforceAccountDetails.class);

	@Autowired
	private SalesforceConstants salesforceConstants;

	@Autowired
	private MakeCompositeRequest makeCompositeRequest;

	/**
	 * Get the details of an account.
	 * @param user
	 * @param accountId
	 * @return GetAccountDetailsFormatterDto
	 **/
	public GetAccountDetailsFormatterDto getAccountDetails(User user, String accountId) {

		logger.info("Salesforce getAccountDetails action called");

		String salesforceUserId = user.getExternalUserId();

		String getAccountDetailsUrl = salesforceConstants.salesforceAccountByIdUrl(accountId);
		CompositeRequestDto compositeReq1 = new CompositeRequestDto("GET", getAccountDetailsUrl, "GetAccountDetails");

		String describeAccountUrl = salesforceConstants.describeAccountLayoutPath();
		CompositeRequestDto compositeReq2 = new CompositeRequestDto("GET", describeAccountUrl, "DescribeAccountLayout");

		SalesforceQueryBuilder salesforceQuery = new SalesforceQueryBuilder();
		String query = salesforceQuery.getAccountContactsQuery(accountId);
		String queryUrl = salesforceConstants.queryUrlPath() + query;
		CompositeRequestDto compositeReq3 = new CompositeRequestDto("GET", queryUrl, "GetAccountContacts");

		List<CompositeRequestDto> compositeRequests = new ArrayList<CompositeRequestDto>();
		compositeRequests.add(compositeReq1);
		compositeRequests.add(compositeReq2);
		compositeRequests.add(compositeReq3);

		HttpClient.HttpResponse response = makeCompositeRequest.makePostRequest(compositeRequests, salesforceUserId);

		return parseResponse(response.getResponseBody(), accountId);

	}

	/**
	 * Parse Response
	 * @param responseBody
	 * @return GetAccountDetailsFormatterDto
	 **/
	public GetAccountDetailsFormatterDto parseResponse(String responseBody, String accountId) {

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

		Map<String, Boolean> requiredFields = getRequiredAccountFields(rootNode);

		GetAccountDetailsFormatterDto getAccountDetailsFormatterDto = new GetAccountDetailsFormatterDto();
		getAccountDetails(rootNode, requiredFields, getAccountDetailsFormatterDto);
		getContactDetails(rootNode, getAccountDetailsFormatterDto, accountId);

		return getAccountDetailsFormatterDto;
	}

	public Map<String, Boolean> getRequiredAccountFields(JsonNode rootNode) {
		JsonNode describeAccountResponseBody = rootNode.get("compositeResponse")
			.get(1)
			.get("body")
			.get("layouts")
			.get(0)
			.get("detailLayoutSections")
			.get(0)
			.get("layoutRows");

		Map<String, Boolean> requiredFields = new HashMap<String, Boolean>();
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

				requiredFields.put(salesforceDescribeAccountDto.getName(), true);
			}
		}

		return requiredFields;
	}

	public void getAccountDetails(JsonNode rootNode, Map<String, Boolean> requiredFields,
			GetAccountDetailsFormatterDto getAccountDetailsFormatterDto) {
		JsonNode getAccountDetailsResponseBody = rootNode.get("compositeResponse").get(0).get("body");

		Iterator<Entry<String, JsonNode>> fieldsIterator = getAccountDetailsResponseBody.fields();

		AccountEntity accountEntity = new AccountEntity();
		accountEntity.setId(getAccountDetailsResponseBody.get("Id").asText());
		accountEntity.setName(getAccountDetailsResponseBody.get("Name").asText());
		accountEntity.setAccountContactAssociationsId(getAccountDetailsResponseBody.get("Id").asText());

		Map<String, Object> additionalFields = new HashMap<String, Object>();

		while (fieldsIterator.hasNext()) {
			Entry<String, JsonNode> entry = fieldsIterator.next();

			String key = entry.getKey();
			if (!requiredFields.containsKey(key) || key == "Name") {
				continue;
			}

			JsonNode valueNode = entry.getValue();
			if (valueNode.isNull()) {
				continue;
			}

			additionalFields.put(key, valueNode.asText());
		}

		accountEntity.setAdditionalFields(additionalFields);
		getAccountDetailsFormatterDto.setAccount(accountEntity);
	}

	public void getContactDetails(JsonNode rootNode, GetAccountDetailsFormatterDto getAccountDetailsFormatterDto,
			String accountId) {
		JsonNode getAccountContactsResponseBody = rootNode.get("compositeResponse").get(2).get("body").get("records");

		List<String> contactIds = new ArrayList<String>();
		Map<String, ContactEntity> contactMapById = new HashMap<>();
		for (JsonNode recordNode : getAccountContactsResponseBody) {
			String contactId = recordNode.get("Id").asText();

			contactIds.add(contactId);

			ContactEntity contactEntity = new ContactEntity();
			contactEntity.setId(contactId);
			contactEntity.setName(recordNode.get("Name").asText());

			Map<String, Object> additionalFields = new HashMap<String, Object>();
			if (!recordNode.get("Title").isNull()) {
				additionalFields.put("title", recordNode.get("Title").asText());
			}
			if (!recordNode.get("Email").isNull()) {
				additionalFields.put("email", recordNode.get("Email").asText());
			}
			if (!recordNode.get("Phone").isNull()) {
				additionalFields.put("phone", recordNode.get("Phone").asText());
			}

			contactEntity.setAdditionalFields(additionalFields);
			contactMapById.put(contactId, contactEntity);
		}

		AccountContactAssociationsEntity accountContactAssociationsEntity = new AccountContactAssociationsEntity();
		accountContactAssociationsEntity.setContactIds(contactIds);

		Map<String, AccountContactAssociationsEntity> accountContactAssociationsMapById = new HashMap<>();
		accountContactAssociationsMapById.put(accountId, accountContactAssociationsEntity);

		getAccountDetailsFormatterDto.setAccountContactAssociationsMapById(accountContactAssociationsMapById);
		getAccountDetailsFormatterDto.setContactMapById(contactMapById);

	}

}
