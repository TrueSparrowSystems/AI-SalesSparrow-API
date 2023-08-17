package com.salessparrow.api.services.accounts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.formatter.GetAccountsFormatterDto;
import com.salessparrow.api.dto.requestMapper.GetAccountsDto;
import com.salessparrow.api.lib.crmActions.getAccounts.GetAccountsFactory;

import jakarta.servlet.http.HttpServletRequest;

/**
 * GetAccountListService is a service class for the GetAccounts action for the CRM.
 */
@Service
public class GetAccountListService {
  @Autowired
  private GetAccountsFactory getAccountsFactory;
  
  /**
     * Get the list of accounts for a given search term
     * @param request
     * @param getAccountsDto
     * 
     * @return GetAccountsFormatterDto
     **/
  public GetAccountsFormatterDto getAccounts(HttpServletRequest request, GetAccountsDto getAccountsDto) {
    User user = (User) request.getAttribute("user");

    String formattedSearchString = formatSearchString(getAccountsDto.getQ());
    return getAccountsFactory.getAccounts(user, formattedSearchString);
  }

  private String formatSearchString(String q) {
    return q.trim();
  }
}
