package com.salessparrow.api.lib.crmActions.getAccounts;

import org.springframework.stereotype.Component;

import com.salessparrow.api.domain.User;
import com.salessparrow.api.dto.formatter.GetAccountsFormatterDto;

/**
 * GetAccounts is an interface for the GetAccounts action for the CRM.
 */
@Component
public interface GetAccounts {

	public GetAccountsFormatterDto getAccounts(User user, String searchTerm, String viewKind, int offset);

}
