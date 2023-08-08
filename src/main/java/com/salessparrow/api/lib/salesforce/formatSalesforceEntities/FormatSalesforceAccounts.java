package com.salessparrow.api.lib.salesforce.formatSalesforceEntities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.salessparrow.api.dto.entities.AccountEntity;
import com.salessparrow.api.dto.formatter.GetAccountsFormatterDto;
import com.salessparrow.api.lib.Util;

public class FormatSalesforceAccounts {

  public GetAccountsFormatterDto formatAccounts(String responseBody) {
    
    List<String> accountIds = new ArrayList<String>();
    AccountEntity accountEntity = new AccountEntity();
    Map<String, AccountEntity> accountEntities = new HashMap<>();

    Util util = new Util();
    JsonNode rootNode = util.getJsonNode(responseBody);
    JsonNode recordsNode = rootNode.get("compositeResponse").get(0).get("body").get("records");

    for (JsonNode recordNode : recordsNode) {
      String accountId = recordNode.get("Id").asText();
      String accountName = recordNode.get("Name").asText();

      accountIds.add(accountId);

      accountEntity.setId(accountId);
      accountEntity.setName(accountName);
      accountEntities.put(accountId, accountEntity);
    }

    GetAccountsFormatterDto getAccountsResponse = new GetAccountsFormatterDto();
    getAccountsResponse.setAccountMapById(accountEntities);
    getAccountsResponse.setAccountIds(accountIds.toArray(new String[accountIds.size()]));

    return getAccountsResponse;
  }
}
