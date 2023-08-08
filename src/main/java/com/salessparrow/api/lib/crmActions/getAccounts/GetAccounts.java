package com.salessparrow.api.lib.crmActions.getAccounts;

import org.springframework.stereotype.Component;

import com.salessparrow.api.domain.SalesforceUser;
import com.salessparrow.api.dto.formatter.GetAccountsFormatterDto;

@Component
public interface GetAccounts {

  public GetAccountsFormatterDto getAccounts(SalesforceUser user, String searchTerm);
}
