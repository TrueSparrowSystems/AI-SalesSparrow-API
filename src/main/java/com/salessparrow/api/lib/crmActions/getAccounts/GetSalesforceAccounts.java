package com.salessparrow.api.lib.crmActions.getAccounts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.entities.AccountEntity;
import com.salessparrow.api.dto.formatter.GetAccountsFormatterDto;
import com.salessparrow.api.lib.Util;
import com.salessparrow.api.lib.globalConstants.SalesforceConstants;
import com.salessparrow.api.lib.httpLib.HttpClient;
import com.salessparrow.api.lib.salesforce.dto.CompositeRequestDto;
import com.salessparrow.api.lib.salesforce.dto.SalesforceAccountDto;
import com.salessparrow.api.lib.salesforce.helper.MakeCompositeRequest;
import com.salessparrow.api.lib.salesforce.helper.SalesforceQueryBuilder;

@Component
public class GetSalesforceAccounts implements GetAccounts{
  @Autowired
  private SalesforceConstants salesforceConstants;

  @Autowired
  private MakeCompositeRequest makeCompositeRequest;

  /**
   * Get the list of accounts for a given search term
   * 
   * @param user
   * @param searchTerm
   * 
   * @return GetAccountsFormatterDto
  **/
  public GetAccountsFormatterDto getAccounts(User user, String searchTerm) {
    String salesforceUserId = user.getExternalUserId();

    SalesforceQueryBuilder salesforceQuery = new SalesforceQueryBuilder();
    String query = salesforceQuery.getAccountsQuery(searchTerm);

    String url = salesforceConstants.queryUrlPath() + query;

    CompositeRequestDto compositeReq = new CompositeRequestDto("GET", url, "getAccounts");

    List<CompositeRequestDto> compositeRequests = new ArrayList<CompositeRequestDto>();
    compositeRequests.add(compositeReq);

    HttpClient.HttpResponse response = makeCompositeRequest.makePostRequest(compositeRequests, salesforceUserId);

    return parseResponse(response.getResponseBody());
  }

  /**
   * Parse Response
   * 
   * @param responseBody
   * 
   * @return GetAccountsFormatterDto
  **/
  public GetAccountsFormatterDto parseResponse(String responseBody) {
    
    List<String> accountIds = new ArrayList<String>();
    Map<String, AccountEntity> accountIdToEntityMap = new HashMap<>();

    Util util = new Util();
    JsonNode rootNode = util.getJsonNode(responseBody);
    JsonNode recordsNode = rootNode.get("compositeResponse").get(0).get("body").get("records");

    for (JsonNode recordNode : recordsNode) {
      ObjectMapper mapper = new ObjectMapper();
      mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
      SalesforceAccountDto salesforceAccount = mapper.convertValue(recordNode, SalesforceAccountDto.class);
      AccountEntity accountEntity = salesforceAccount.getAccountEntity();

      accountIds.add(accountEntity.getId());
      accountIdToEntityMap.put(accountEntity.getId(), accountEntity);
    }

    GetAccountsFormatterDto getAccountsResponse = new GetAccountsFormatterDto();
    getAccountsResponse.setAccountMapById(accountIdToEntityMap);
    getAccountsResponse.setAccountIds(accountIds);

    return getAccountsResponse;
  }
}
