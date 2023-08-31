package com.salessparrow.api.lib.crmActions.getCrmOrganizationUsers;

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
import com.salessparrow.api.dto.entities.CrmOrganizationUserEntity;
import com.salessparrow.api.dto.formatter.GetCrmOrganizationUsersFormatterDto;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.Util;
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.globalConstants.SalesforceConstants;
import com.salessparrow.api.lib.httpLib.HttpClient;
import com.salessparrow.api.lib.salesforce.dto.CompositeRequestDto;
import com.salessparrow.api.lib.salesforce.dto.SalesforceCrmOrganizationUserDto;
import com.salessparrow.api.lib.salesforce.helper.MakeCompositeRequest;
import com.salessparrow.api.lib.salesforce.helper.SalesforceQueryBuilder;

/**
 * GetCrmOrganizationUsersFactory class for the getCrmOrganizationUsers action.
 *
 */
@Component
public class GetSalesforceCrmOrganizationUsers implements GetCrmOrganizationUsers {

	Logger logger = LoggerFactory.getLogger(GetSalesforceCrmOrganizationUsers.class);

	@Autowired
	private SalesforceConstants salesforceConstants;

	@Autowired
	private MakeCompositeRequest makeCompositeRequest;

	/**
	 * getCrmOrganizationUsers method for the getCrmOrganizationUsers action.
	 * @param user
	 * @param searchTerm
	 * @return GetCrmOrganizationUsersFormatterDto
	 */
	public GetCrmOrganizationUsersFormatterDto getCrmOrganizationUsers(User user, String searchTerm) {
		String salesforceUserId = user.getExternalUserId();

		SalesforceQueryBuilder salesforceQuery = new SalesforceQueryBuilder();
		String query = salesforceQuery.getCrmOrganizationUsersQuery(searchTerm);

		String url = salesforceConstants.queryUrlPath() + query;

		CompositeRequestDto compositeReq = new CompositeRequestDto("GET", url, "getCrmOrganizationUsers");

		List<CompositeRequestDto> compositeRequests = new ArrayList<CompositeRequestDto>();
		compositeRequests.add(compositeReq);

		HttpClient.HttpResponse response = makeCompositeRequest.makePostRequest(compositeRequests, salesforceUserId);

		return parseResponse(response.getResponseBody());
	}

	/**
	 * parseResponse method for the getCrmOrganizationUsers action.
	 * @param responseBody
	 * @return GetCrmOrganizationUsersFormatterDto
	 */
	private GetCrmOrganizationUsersFormatterDto parseResponse(String responseBody) {
		List<String> crmOrganizationUserIds = new ArrayList<String>();
		Map<String, CrmOrganizationUserEntity> crmOrganizationUserMap = new HashMap<String, CrmOrganizationUserEntity>();

		logger.info("Parsing response from salesforce");

		Util util = new Util();
		JsonNode rootNode = util.getJsonNode(responseBody);

		JsonNode httpStatusCodeNode = rootNode.get("compositeResponse").get(0).get("httpStatusCode");

		if (httpStatusCodeNode.asInt() != 200 && httpStatusCodeNode.asInt() != 201) {
			throw new CustomException(
					new ErrorObject("l_ca_ga_gsa_pr_1", "bad_request", "Error in fetching accounts from salesforce"));
		}

		JsonNode recordsNode = rootNode.get("compositeResponse").get(0).get("body").get("records");

		for (JsonNode recordNode : recordsNode) {
			ObjectMapper mapper = new ObjectMapper();
			mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
			SalesforceCrmOrganizationUserDto salesforceCrmOrganization = mapper.convertValue(recordNode,
					SalesforceCrmOrganizationUserDto.class);
			CrmOrganizationUserEntity accountCrmOrganizationUser = salesforceCrmOrganization
				.getCrmOrganizationUserEntity();

			crmOrganizationUserIds.add(accountCrmOrganizationUser.getId());
			crmOrganizationUserMap.put(accountCrmOrganizationUser.getId(), accountCrmOrganizationUser);
		}

		GetCrmOrganizationUsersFormatterDto getCrmOrganizationUsersResponse = new GetCrmOrganizationUsersFormatterDto();
		getCrmOrganizationUsersResponse.setCrmOrganizationUserIds(crmOrganizationUserIds);
		getCrmOrganizationUsersResponse.setCrmOrganizationUserMapById(crmOrganizationUserMap);

		return getCrmOrganizationUsersResponse;
	}

}
