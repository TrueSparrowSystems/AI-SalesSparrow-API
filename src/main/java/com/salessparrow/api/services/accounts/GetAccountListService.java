package com.salessparrow.api.services.accounts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.formatter.GetAccountsFormatterDto;
import com.salessparrow.api.lib.crmActions.getAccounts.GetAccountsFactory;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class GetAccountListService {
  @Autowired
  private GetAccountsFactory getAccountsFactory;
  
  public GetAccountsFormatterDto getAccounts(HttpServletRequest request, String q) {
    User user = (User) request.getAttribute("user");

    String formattedSearchString = formatSearchString(q);
    return getAccountsFactory.getAccounts(user, formattedSearchString);
  }

  private String formatSearchString(String q) {
    return q.trim();
  }
}
