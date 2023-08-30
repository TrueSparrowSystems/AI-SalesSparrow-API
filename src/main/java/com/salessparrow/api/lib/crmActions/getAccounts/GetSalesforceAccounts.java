package com.salessparrow.api.lib.crmActions.getAccounts;

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
import com.salessparrow.api.dto.entities.AccountContactAssociationsEntity;
import com.salessparrow.api.dto.entities.AccountEntity;
import com.salessparrow.api.dto.entities.ContactEntity;
import com.salessparrow.api.dto.formatter.GetAccountsFormatterDto;
import com.salessparrow.api.exception.CustomException;
import com.salessparrow.api.lib.UserLoginCookieAuth;
import com.salessparrow.api.lib.Util;
import com.salessparrow.api.lib.errorLib.ErrorObject;
import com.salessparrow.api.lib.globalConstants.AccountConstants;
import com.salessparrow.api.lib.globalConstants.SalesforceConstants;
import com.salessparrow.api.lib.httpLib.HttpClient;
import com.salessparrow.api.lib.salesforce.dto.CompositeRequestDto;
import com.salessparrow.api.lib.salesforce.dto.SalesforceAccountDto;
import com.salessparrow.api.lib.salesforce.dto.SalesforceContactDto;
import com.salessparrow.api.lib.salesforce.helper.MakeCompositeRequest;
import com.salessparrow.api.lib.salesforce.helper.SalesforceQueryBuilder;

/**
 * GetSalesforceAccounts is a class for the GetAccounts service for the
 * Salesforce CRM.
 **/
@Component
public class GetSalesforceAccounts implements GetAccounts {
  @Autowired
  private SalesforceConstants salesforceConstants;

  @Autowired
  private MakeCompositeRequest makeCompositeRequest;

  @Autowired
  private Util util;

  Logger logger = LoggerFactory.getLogger(UserLoginCookieAuth.class);

  /**
   * Get the list of accounts for a given search term
   * 
   * @param user
   * @param searchTerm
   * 
   * @return GetAccountsFormatterDto
   **/
  public GetAccountsFormatterDto getAccounts(User user, String searchTerm, String viewKind, int offset) {
    String salesforceUserId = user.getExternalUserId();

    SalesforceQueryBuilder salesforceQuery = new SalesforceQueryBuilder();
    String query = null;

    if (viewKind.equals(AccountConstants.FEED_VIEW_KIND)) {
      logger.info("View kind is feed");
      query = salesforceQuery.getAccountFeedQuery(AccountConstants.PAGINATION_LIMIT, offset);
    } else if (viewKind.equals(AccountConstants.BASIC_VIEW_KIND)) {
      logger.info("View kind is basic");
      query = salesforceQuery.getAccountsQuery(searchTerm);
    } else {
      throw new CustomException(
          new ErrorObject(
              "l_ca_ga_gsa_ga_1",
              "something_went_wrong",
              "Invalid view kind."));
    }

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
    Map<String, ContactEntity> contactMapById = new HashMap<>();
    Map<String, AccountContactAssociationsEntity> accountContactAssociationsMapById = new HashMap<>();

    JsonNode rootNode = util.getJsonNode(responseBody);

    JsonNode httpStatusCodeNode = rootNode.get("compositeResponse").get(0).get("httpStatusCode");

    if (httpStatusCodeNode.asInt() != 200 && httpStatusCodeNode.asInt() != 201) {
      throw new CustomException(
          new ErrorObject(
              "l_ca_ga_gsa_pr_1",
              "something_went_wrong",
              "Error in fetching accounts from salesforce"));
    }

    JsonNode recordsNode = rootNode.get("compositeResponse").get(0).get("body").get("records");

    for (JsonNode recordNode : recordsNode) {
      ObjectMapper mapper = new ObjectMapper();
      mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
      SalesforceAccountDto salesforceAccount = mapper.convertValue(recordNode, SalesforceAccountDto.class);

      AccountEntity accountEntity = salesforceAccount.getAccountEntity();
      accountIds.add(accountEntity.getId());
      accountIdToEntityMap.put(accountEntity.getId(), accountEntity);

      if (salesforceAccount.getContacts() != null && salesforceAccount.getContacts().getRecords() != null
          && salesforceAccount.getContacts().getRecords().size() > 0) {
        handleContacts(salesforceAccount, contactMapById, accountContactAssociationsMapById);
      }
    }

    GetAccountsFormatterDto getAccountsResponse = new GetAccountsFormatterDto();
    getAccountsResponse.setAccountMapById(accountIdToEntityMap);
    getAccountsResponse.setAccountIds(accountIds);
    getAccountsResponse.setContactMapById(contactMapById);
    getAccountsResponse.setAccountContactAssociationsMapById(accountContactAssociationsMapById);

    return getAccountsResponse;
  }

  private void handleContacts(SalesforceAccountDto salesforceAccount, Map<String, ContactEntity> contactMapById,
      Map<String, AccountContactAssociationsEntity> accountContactAssociationsMapById) {
    List<String> contactIds = new ArrayList<String>();

    for (SalesforceContactDto contact : salesforceAccount.getContacts().getRecords()) {
      ContactEntity contactEntity = contact.getContactEntity();
      contactMapById.put(contactEntity.getId(), contactEntity);
      contactIds.add(contactEntity.getId());
    }

    AccountContactAssociationsEntity accountContactAssociationsEntity = new AccountContactAssociationsEntity();
    accountContactAssociationsEntity.setContactIds(contactIds);
    accountContactAssociationsMapById.put(salesforceAccount.getAccountEntity().getId(),
        accountContactAssociationsEntity);
  }
}
