package com.salessparrow.api.lib.crmActions.getAccounts;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salessparrow.api.domain.SalesforceUser;
import com.salessparrow.api.dto.formatter.GetAccountsFormatterDto;
import com.salessparrow.api.lib.globalConstants.SalesforceConstants;
import com.salessparrow.api.lib.httpLib.HttpClient;
import com.salessparrow.api.lib.salesforce.CompositeRequest;
import com.salessparrow.api.lib.salesforce.MakeCompositeRequest;
import com.salessparrow.api.lib.salesforce.SalesforceLib;
import com.salessparrow.api.lib.salesforce.formatSalesforceEntities.FormatSalesforceAccounts;

@Component
public class GetSalesforceAccounts implements GetAccounts{
  @Autowired
  private SalesforceConstants salesforceConstants;

  @Autowired
  private MakeCompositeRequest makeCompositeRequest;

  public GetAccountsFormatterDto getAccounts(SalesforceUser user, String searchTerm) {
    String salesforceUserId = user.getExternalUserId();

    SalesforceLib salesforceLib = new SalesforceLib();
    String query = salesforceLib.getAccountsQuery(searchTerm);

    String url = salesforceConstants.queryUrlPath() + query;

    CompositeRequest compositeReq = new CompositeRequest("GET", url, "getAccounts");

    List<CompositeRequest> compositeRequests = new ArrayList<CompositeRequest>();
    compositeRequests.add(compositeReq);

    HttpClient.HttpResponse response = makeCompositeRequest.makePostRequest(compositeRequests, salesforceUserId );

    FormatSalesforceAccounts formatSalesforceAccounts = new FormatSalesforceAccounts();
    return formatSalesforceAccounts.formatAccounts(response.getResponseBody());
  }
}
