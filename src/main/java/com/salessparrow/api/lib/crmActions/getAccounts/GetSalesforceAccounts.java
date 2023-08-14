package com.salessparrow.api.lib.crmActions.getAccounts;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.formatter.GetAccountsFormatterDto;
import com.salessparrow.api.lib.globalConstants.SalesforceConstants;
import com.salessparrow.api.lib.httpLib.HttpClient;
import com.salessparrow.api.lib.salesforce.dto.CompositeRequest;
import com.salessparrow.api.lib.salesforce.formatSalesforceEntities.FormatSalesforceAccounts;
import com.salessparrow.api.lib.salesforce.helper.MakeCompositeRequest;
import com.salessparrow.api.lib.salesforce.helper.SalesforceQueries;

@Component
public class GetSalesforceAccounts implements GetAccounts{
  @Autowired
  private SalesforceConstants salesforceConstants;

  @Autowired
  private MakeCompositeRequest makeCompositeRequest;

  @Autowired
  private FormatSalesforceAccounts formatSalesforceAccounts;

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

    SalesforceQueries salesforceQueries = new SalesforceQueries();
    String query = salesforceQueries.getAccountsQuery(searchTerm);

    String url = salesforceConstants.queryUrlPath() + query;

    CompositeRequest compositeReq = new CompositeRequest("GET", url, "getAccounts");

    List<CompositeRequest> compositeRequests = new ArrayList<CompositeRequest>();
    compositeRequests.add(compositeReq);

    HttpClient.HttpResponse response = makeCompositeRequest.makePostRequest(compositeRequests, salesforceUserId );

    return formatSalesforceAccounts.formatAccounts(response.getResponseBody());
  }
}
