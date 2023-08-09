package com.salessparrow.api.lib.crmActions.getAccounts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.salessparrow.api.domain.SalesforceUser;
import com.salessparrow.api.dto.formatter.GetAccountsFormatterDto;

@Component
public class GetAccountsFactory {
  @Autowired
  private GetSalesforceAccounts getSalesforceAccounts;

  public GetAccountsFormatterDto getAccounts(SalesforceUser user, String searchTerm) {

    // Add if condition based on kind to determine which CRM to use
    GetAccountsFormatterDto getAccountsResponse = null;
    getAccountsResponse = getSalesforceAccounts.getAccounts(user, searchTerm);
  
    return getAccountsResponse;
  }
}
