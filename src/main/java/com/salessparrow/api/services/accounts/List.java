package com.salessparrow.api.services.accounts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salessparrow.api.domain.SalesforceUser;
import com.salessparrow.api.dto.formatter.GetAccountsFormatterDto;
import com.salessparrow.api.lib.crmActions.getAccounts.GetAccountsFactory;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class List {
  @Autowired
  private GetAccountsFactory getAccountsFactory;
  
  public GetAccountsFormatterDto getAccounts(HttpServletRequest request, String q) {
    SalesforceUser user = (SalesforceUser) request.getAttribute("user");

    return getAccountsFactory.getAccounts(user, q);
  } 
}
